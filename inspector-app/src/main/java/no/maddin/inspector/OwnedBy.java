package no.maddin.inspector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type="OWNED_BY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnedBy {

    @GraphId @Index(primary = true, unique = true)
    private Long id;

    @StartNode
    private Instance instance;

    @EndNode
    private Instance owner;

    @Index
    private String fieldName;
}
