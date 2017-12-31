package no.maddin.inspector;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class Neo4jNodeIdGenerator implements NodeIdGenerator {
    private final AtomicLong id = new AtomicLong(0);

    @Override
    public long nodeId() {
        return id.incrementAndGet();
    }

    @Override
    public void setNodeId(long start)  {
        id.set(start);
    }
}
