package no.maddin.inspector;

import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;

/**
 * OwnedBys has to be excluded from equals and hashCode to avoid stack overflow due to endless recursion.
 */
@NodeEntity
@Data
@ToString(exclude = {"ownedBys"})
@EqualsAndHashCode(exclude = {"ownedBys"})
public class Instance {
    @GraphId @Index(primary = true, unique = true)
    private Long id;

    @Index(unique=true)
    private Long hashValue;

    private String type;

    private java.util.Set<OwnedBy> ownedBys = new HashSet<>();
}

