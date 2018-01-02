package no.maddin.inspector;

public class Runner implements Runnable {
    @Override
    public void run() {

        int count = 0;
        while(true) {
            try {
                Thread.sleep(10_000L);
                System.out.println("Slept " + (++count) + " times");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
