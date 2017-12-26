package no.maddin.inspector;


import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html?is-external=true
 *
 */
@Slf4j
@RequiredArgsConstructor
public class Inspector implements Runnable {

    private final String pid;

    @Override
    public void run() {
        attachAgent();
    }

    private void attachAgent() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        try (
            ServerSocket listener = new ServerSocket(0, 50, InetAddress.getLoopbackAddress());
            AgentLib agentLib = AgentLib.fromClassPath("/META-INF/lib/openjdk-agent.jar")
        ) {
            int serverPort = listener.getLocalPort();
            VirtualMachine vm = VirtualMachine.attach(pid);
            try {
                service.submit(() -> readFromSocket(listener));
                String includes = "(no\\.maddin\\..+|org\\.springframework\\..+)";
                String argString = String.format("%d %s", serverPort, includes);
                vm.loadAgent(agentLib.getPath(), argString);
            } finally {
                service.shutdown();
                service.awaitTermination(60, TimeUnit.MINUTES);
                vm.detach();
            }
        } catch (IOException | AgentLoadException | AgentInitializationException | AttachNotSupportedException | InterruptedException ex) {
            log.info("attaching agent", ex);
        }
    }

    private static void readFromSocket(ServerSocket listener) {
        try (
            Socket socket = listener.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                InspectData data = InspectData.fromAgentString(line);
                log.info(data.toString());//System.out.println(data);
                if ("EXIT".equals(line)) {
                    break;
                }
            }
        } catch (IOException e) {
            log.error("reading from socket", e);
        }
    }

    private static class AgentLib implements AutoCloseable {

        private static AgentLib fromClassPath(String location) throws IOException {
            try (
                InputStream is = Inspector.class.getResourceAsStream(location);
            ) {
                Path tempFile = Files.createTempFile("inspect-agent-", ".jar");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                return new AgentLib(tempFile);
            }
        }

        private final Path agentLibPath;

        private AgentLib(Path agentLib) {
            this.agentLibPath = agentLib;
        }

        public String getPath() {
            return agentLibPath.toAbsolutePath().toString();
        }

        @Override
        public void close() throws IOException {
            Files.delete(agentLibPath);
        }
    }
}
