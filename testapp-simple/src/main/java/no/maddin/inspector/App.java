package no.maddin.inspector;

import java.lang.management.ManagementFactory;

/**
 * Hello world!
 *
 */
public class App {

    private static Runner THE_RUNNER;

    public static void main( String[] args ) {
        System.out.println("Running app with PID " + getPid());

        THE_RUNNER = new Runner();

        synchronized(THE_RUNNER) {

            new Thread(THE_RUNNER).start();

            try {
                THE_RUNNER.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPid() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        return jvmName.split("@")[0];
    }
}
