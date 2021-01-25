package net.fisenko.utils.ids.snowflake.timeSource.impl;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGenerator;
import net.fisenko.utils.ids.snowflake.timeSource.TimeSource;
import org.apache.commons.lang3.time.StopWatch;

/**
 * Provides time data to an {@link SnowflakeIdGenerator}. This timesource uses a {@link StopWatch} for timekeeping.
 */
public abstract class StopwatchTimeSource implements TimeSource {

    private static final StopWatch stopWatch = StopWatch.create();
    private static final OffsetDateTime defaultOffset = OffsetDateTime.now(ZoneOffset.UTC);
    private final Duration offset;
    private final OffsetDateTime epoch;
    private final Duration tickDuration;

    /**
     * Initializes a new {@link StopwatchTimeSource} object.
     *
     * @param epoch        The epoch to use as an offset from now.
     * @param tickDuration The duration of a single tick for this timesource.
     */
    public StopwatchTimeSource(OffsetDateTime epoch, Duration tickDuration) {
        this.epoch = epoch;
        long epochMilli = epoch.toInstant().toEpochMilli();
        long defaultOffsetMilli = defaultOffset.toInstant().toEpochMilli();
        this.offset = Duration.ofMillis(defaultOffsetMilli - epochMilli);
        this.tickDuration = tickDuration;

        // Start (or resume) stopwatch
        if (!stopWatch.isStarted()) {
            stopWatch.start();
        }
    }

    /**
     * Gets the elapsed time since this {@link TimeSource} was initialized.
     *
     * @return Returns the duration.
     */
    protected static Duration getElapsed() {
        return Duration.ofNanos(stopWatch.getNanoTime());
    }

    /**
     * Gets the epoch of the {@link TimeSource}.
     *
     * @return Returns the offset;
     */
    public OffsetDateTime getEpoch() {
        return epoch;
    }

    /**
     * Gets the duration of a single tick.
     *
     * @return Returns the duration.
     */
    public Duration getTickDuration() {
        return tickDuration;
    }

    /**
     * Gets the current number of ticks for the {@link DefaultTimeSourceImpl}
     *
     * @return The current number of ticks to be used by an {@link SnowflakeIdGenerator} when creating an Id.
     */
    public abstract long getTicks();

    /**
     * Gets the offset for this {@link TimeSource} which is defined as the difference of it's creation date and it's epoch which is specified in the object's constructor.
     *
     * @return Returns the duration.
     */
    protected Duration getOffset() {
        return offset;
    }
}