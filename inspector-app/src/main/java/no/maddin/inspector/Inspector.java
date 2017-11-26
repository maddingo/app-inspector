package no.maddin.inspector;


import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html?is-external=true
 *
 */
public class Inspector {
    public static void main(String[] args) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        try (
            ServerSocket listener = new ServerSocket(0, 50, InetAddress.getLoopbackAddress())
        ) {
            int serverPort = listener.getLocalPort();
            VirtualMachine vm = VirtualMachine.attach("5941");
            try {
                service.submit(() -> readFromSocket(listener));
                String includes = "(no\\.maddin\\..+|org\\.springframework\\..+)";
                String argString = String.format("%d %s", serverPort, includes);
                vm.loadAgent("/home/martin/Develop/inspector/openjdkagent/target/openjdk-agent.jar", argString);
            } finally {
                service.shutdown();
                service.awaitTermination(60, TimeUnit.MINUTES);
                vm.detach();
            }
        } catch (IOException | AgentLoadException | AgentInitializationException | AttachNotSupportedException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static void readFromSocket(ServerSocket listener) {
            try (
                Socket socket = listener.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    if ("EXIT".equals(line)) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
