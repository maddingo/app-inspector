package no.maddin.inspect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html#package_description
 *
 * https://docs.oracle.com/javase/8/docs/technotes/guides/instrumentation/index.html
 */
public class Agent {
    public static void agentmain(String agentArgs, Instrumentation inst) {
        Class[] loadedClasses = inst.getAllLoadedClasses();
        String[] args = parseArgs(agentArgs);
        int port = Integer.parseInt(args[0]);
        try (
            Socket s = new Socket("localhost", port);
            PrintStream os = new PrintStream(s.getOutputStream());
        ) {
            InstanceInspector inspector = new InstanceInspector(loadedClasses, (owner, field, value) ->
                onVisit(os, owner, field, value),
                () -> sendStopCommand(os),
                args[1]);
            inspector.start();
            inspector.stopAndWait(6000);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendStopCommand(PrintStream os) {
        os.println();
        os.println("EXIT");
    }

    private static String[] parseArgs(String agentArgs) {
        return agentArgs.split(" ", 2);
    }

    private static void onVisit(PrintStream os, Object owner, Field field, Object fieldValue) {
        Class<?> ownerClass = owner instanceof Class ? (Class<?>) owner : owner.getClass();
        os.printf("%08X(%s) --%s--> %08X(%s)\n", owner.hashCode(), ownerClass.getName(), field.getName(), fieldValue.hashCode(), fieldValue.getClass().getName());
    }
}
