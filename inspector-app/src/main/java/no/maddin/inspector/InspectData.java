package no.maddin.inspector;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class InspectData {
    long ownerHash;
    String ownerClassName;
    String fieldName;
    String targetClassName;
    long instanceHash;
    String fieldClassName;

    public static InspectData fromAgentString(String line) {
        String[] elems = line.split("\\|");

        return InspectData.builder()
            .ownerHash(Long.parseLong(elems[0], 16))
            .ownerClassName(elems[1])
            .fieldName(elems[2])
            .instanceHash(Long.parseLong(elems[3], 16))
            .fieldClassName(elems[4])
            .build();

    }
}
