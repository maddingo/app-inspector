package no.maddin.inspector.testapp;

import org.springframework.boot.ApplicationPid;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@ComponentScan
public class App {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(App.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    private void printPid(ApplicationReadyEvent evt) throws ReflectiveOperationException {
        System.out.println(new ApplicationPid().toString());
    }
}
