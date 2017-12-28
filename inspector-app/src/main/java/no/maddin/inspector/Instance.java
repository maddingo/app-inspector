package no.maddin.inspector;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.annotation.Id;

@NodeEntity
@Data
public class Instance {
    @Id
    private Long id;

    private Long hashValue;

    private String type;

    @Relationship(type="OWNED_BY")
    private java.util.Set<Instance> ownedBy;
}
