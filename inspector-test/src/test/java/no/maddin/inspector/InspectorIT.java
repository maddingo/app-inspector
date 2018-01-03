package no.maddin.inspector;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.ogm.annotation.Relationship;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class InspectorIT {

    @Test
    void countClassA() throws IOException {
        String javaHome = System.getProperty("java.home");
        Process proc = new ProcessBuilder()
            .command(javaHome + "/bin/java", "-jar", "../testapp-simple/target/testapp-simple.jar")
            .start();

        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        String pid = "";
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            if (line.startsWith("Running app with PID ")) {
                pid = line.substring(21);
                break;
            }
        }
        assertThat(pid, is(not(isEmptyString())));

        InspectorApp.main(new String[]{"--pid=" + pid});

        Map<String, Object> map = new HashMap<>();
        try (
            Driver driver = GraphDatabase.driver("bolt://172.17.0.3:7687", AuthTokens.basic("neo4j", "secret"));
            Session session = driver.session();
        ) {
            Transaction tr = session.beginTransaction();
            StatementResult result = tr.run("MATCH (n1)-[r]->(n2) RETURN n1, n2, r");
            while (result.hasNext()) {
                Record rec = result.next();
                map.putAll(rec.asMap());
            }
            tr.close();
        }
        assertThat(map.size(), is(equalTo(3)));
        assertThat(map.get("r"), instanceOf(org.neo4j.driver.v1.types.Relationship.class));
        assertThat(map.get("n1"), instanceOf(Node.class));
        assertThat(map.get("n2"), instanceOf(Node.class));
    }
}
