package no.maddin.inspector;

@FunctionalInterface
public interface InspectDataPersister {
    void persist(InspectData data);
}
