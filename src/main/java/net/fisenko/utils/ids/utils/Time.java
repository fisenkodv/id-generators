package net.fisenko.utils.ids.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Time {

    public final static OffsetDateTime UNIX_EPOCH = OffsetDateTime.of(LocalDateTime.of(1970, 1, 1, 0, 0, 0), ZoneOffset.UTC);

    public static long toUnixTimeMilli(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toEpochSecond() * 1000;
    }

    public static OffsetDateTime fromUnixTimeMilli(long milli) {
        return Instant.ofEpochMilli(milli).atOffset(ZoneOffset.ofTotalSeconds(0));
    }
}
