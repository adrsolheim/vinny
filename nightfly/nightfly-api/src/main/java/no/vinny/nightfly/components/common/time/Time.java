package no.vinny.nightfly.components.common.time;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Time {

    public static OffsetDateTime now() {
        return OffsetDateTime.now(ZoneId.of("UTC"));
    }

    public static ZonedDateTime toOsloTime(Timestamp timestamp) {
        return timestamp == null ? null : ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("Europe/Oslo"));
    }
}
