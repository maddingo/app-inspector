package no.maddin.inspector;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan
@Configuration
@EnableTransactionManagement
@EnableNeo4jRepositories("no.maddin.inspector")
@EnableAutoConfiguration
public class Neo4jConfiguration {

    @Autowired
    private Environment env;

//    @Bean
//    public SessionFactory getSessionFactory() {
//        return new SessionFactory(configuration(), Instance.class);
//    }

//    @Bean
//    public Neo4jTransactionManager transactionManager() throws Exception {
//        return new Neo4jTransactionManager(getSessionFactory());
//    }

//    @Bean
//    public org.neo4j.ogm.config.Configuration configuration() {
//        org.neo4j.ogm.config.Configuration conf =
//            new org.neo4j.ogm.config.Configuration();
//
//        if (env.containsProperty("spring.data.neo4j.driver")) {
//            conf.set("spring.data.neo4j.driver", env.getProperty("spring.data.neo4j.driver"));
//        }
//        if (env.containsProperty("spring.data.neo4j.password")) {
//            conf.set("spring.data.neo4j.password", env.getProperty("spring.data.neo4j.password"));
//        }
//        return conf;
//    }

//    @Bean
//    public Object neo4jMappingContext() {
//        throw new UnsupportedOperationException();
//    }
}
