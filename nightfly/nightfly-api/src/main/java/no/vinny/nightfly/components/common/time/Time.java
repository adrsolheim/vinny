package no.vinny.nightfly.components.common.time;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Time {

    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Europe/Oslo");

    public static OffsetDateTime now() {
        return OffsetDateTime.now(UTC);
    }

    public static ZonedDateTime zoned(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.atZoneSameInstant(DEFAULT_ZONE);
    }


    public static ZonedDateTime toOsloTime(Timestamp timestamp) {
        return timestamp == null ? null : ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("Europe/Oslo"));
    }
}
