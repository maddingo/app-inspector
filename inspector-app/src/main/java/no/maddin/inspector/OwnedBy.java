package no.maddin.inspector;

import lombok.*;
import org.neo4j.ogm.annotation.*;

/**
 * See https://stackoverflow.com/a/36281630/555366 on saving IDs.
 */
@RelationshipEntity(type="OWNED_BY")
@ToString
@EqualsAndHashCode
public class OwnedBy {

    @Getter
    private Long id;

    @Getter
    @Setter
    @StartNode
    private Instance instance;

    @Getter
    @Setter
    @EndNode
    private Instance owner;

    @Getter
    @Setter
    @Index
    private String fieldName;
}
