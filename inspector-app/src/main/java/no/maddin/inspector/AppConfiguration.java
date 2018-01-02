package no.maddin.inspector;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class AppConfiguration {

//    @Bean
//    public NodeIdGenerator nodeIdGenerator() {
//        return new Neo4jNodeIdGenerator();
//    }
}
