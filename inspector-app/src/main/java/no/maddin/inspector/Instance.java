package no.maddin.inspector;

import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;

/**
 * OwnedBys has to be excluded from equals and hashCode to avoid stack overflow due to endless recursion.
 *
 * See https://stackoverflow.com/a/36281630/555366 on saving IDs.
 */
@NodeEntity
@ToString(exclude = {"ownedBys"})
@EqualsAndHashCode(exclude = {"ownedBys"})
public class Instance {

    @Getter
    private Long id;

    @Getter
    @Setter
    @Index(unique=true)
    private Long hashValue;

    @Getter
    @Setter
    private String type;

    @Relationship(type = "OWNED_BY")
    @Getter
    private java.util.Set<OwnedBy> ownedBys = new HashSet<>();
//    private OwnedBy ownedBy;
}

