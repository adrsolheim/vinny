package no.vinny.nightfly.components.batch.domain;

public enum VolumeStatus {
    NOT_EMPTY("Not empty"),
    EMPTY("Empty");

    String value;

    private VolumeStatus(String value) {
        this.value = value;
    }
}
