package io.toolongname.sandcastle.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Time {
    public static final String ASIA_SHANGHAI = "Asia/Shanghai";

    public static String formatTimestampAsISO_OFFSET_DATE_TIME(long milliseconds) {
        return Instant.ofEpochSecond(milliseconds)
                .atZone(ZoneId.of(ASIA_SHANGHAI))
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String formatTimestamp(long milliseconds) {
        return Instant.ofEpochSecond(milliseconds)
                .atZone(ZoneId.of(ASIA_SHANGHAI))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
