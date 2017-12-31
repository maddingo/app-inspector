package no.maddin.inspector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@ComponentScan
public class InspectorApp implements ApplicationRunner {

    private String pid;

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private NodeIdGenerator idGenerator;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(InspectorApp.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("pid")) {
            pid = args.getOptionValues("pid").get(0);
        } else {
            throw new IllegalArgumentException("Missing parameter");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    private void onAppReady(ApplicationReadyEvent evt) throws ReflectiveOperationException {
        if (pid != null) {
            new Inspector(pid, instanceRepository, idGenerator).run();
        }
    }
}
