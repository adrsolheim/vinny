package no.vinny.nightfly.domain.batch;

public enum VolumeStatus {
    NOT_EMPTY("Not empty"),
    EMPTY("Empty");

    String value;

    VolumeStatus(String value) {
        this.value = value;
    }
}
