package net.fisenko.utils.ids.snowflake;

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * Provides the interface for timesources that provide time information to {@link IdGenerator}s.
 */
public interface TimeSource {
    /**
     * Gets the epoch of the {@link TimeSource}.
     * @return Returns date time offset.
     */
    OffsetDateTime getEpoch();

    /**
     * Returns the duration of a single tick.
     * Note: It's up to the {@link TimeSource} to define what a 'tick' is; it may be nanoseconds, milliseconds,
     * seconds or even days or years.
     * @return Returns the duration.
     */
    Duration getTickDuration();

    /**
     * Returns the current number of ticks for the {@link TimeSource}.
     * Note: It's up to the {@link TimeSource} to define what a 'tick' is; it may be nanoseconds, milliseconds,
     * seconds or even days or years.
     * @return The current number of ticks to be used by an {@link IdGenerator} when creating an Id.
     */
    long getTicks();
}