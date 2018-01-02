package no.maddin.inspector;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class Neo4jNodeIdGenerator implements NodeIdGenerator {
    private final AtomicLong nodeId = new AtomicLong(0);
    private final AtomicLong relationshipId = new AtomicLong(0);

    @Override
    public long nodeId() {
        return nodeId.incrementAndGet();
    }

    @Override
    public long relationshipId() {
        return relationshipId.incrementAndGet();
    }

    @Override
    public void setNodeId(long start)  {
        nodeId.set(start);
    }
}
