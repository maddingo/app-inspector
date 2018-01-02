package no.maddin.inspector;

public interface NodeIdGenerator {
    long nodeId();

    long relationshipId();

    void setNodeId(long start);
}
