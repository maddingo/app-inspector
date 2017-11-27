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
    String fieldValue;
    String fieldClassName;
}
