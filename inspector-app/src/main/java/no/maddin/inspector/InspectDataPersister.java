package no.maddin.inspector;

import java.io.IOException;

@FunctionalInterface
public interface InspectDataPersister {
    void persist(InspectData data) throws IOException;
}
