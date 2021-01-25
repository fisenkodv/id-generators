package net.fisenko.utils.ids.snowflake.timeSource;

import java.time.Duration;
import java.time.OffsetDateTime;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGenerator;

/**
 * Provides the interface for timesources that provide time information to {@link SnowflakeIdGenerator}s.
 */
public interface TimeSource {

    /**
     * Returns the epoch of the {@link TimeSource}.
     *
     * @return date time offset
     */
    OffsetDateTime getEpoch();

    /**
     * Returns the duration of a single tick. Note: It's up to the {@link TimeSource} to define what a 'tick' is; it may be nanoseconds, milliseconds, seconds or even days or
     * years.
     *
     * @return the duration.
     */
    Duration getTickDuration();

    /**
     * Returns the current number of ticks for the {@link TimeSource}. Note: It's up to the {@link TimeSource} to define what a 'tick' is; it may be nanoseconds, milliseconds,
     * seconds or even days or years.
     *
     * @return the current number of ticks to be used by an {@link SnowflakeIdGenerator} when creating an Id.
     */
    long getTicks();
}