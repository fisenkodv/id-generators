package net.fisenko.utils.ids.snowflake.timeSource.impl;

import java.time.Duration;
import java.time.OffsetDateTime;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGenerator;

/**
 * Provides time data to an {@link SnowflakeIdGenerator}. Unless specified the default duration of a tick for a {@link DefaultTimeSourceImpl} is 1 nanosecond.
 */
public final class DefaultTimeSourceImpl extends StopwatchTimeSource {

    /**
     * Initializes a new {@link DefaultTimeSourceImpl} object. Note: The default tick duration is 1 millisecond.
     *
     * @param epoch The epoch to use as an offset from now.
     */
    public DefaultTimeSourceImpl(OffsetDateTime epoch) {
        super(epoch, Duration.ofMillis(1));
    }

    /**
     * Initializes a new {@link DefaultTimeSourceImpl} object.
     *
     * @param epoch        The epoch to use as an offset from now.
     * @param tickDuration The duration of a tick for this timesource.
     */
    public DefaultTimeSourceImpl(OffsetDateTime epoch, Duration tickDuration) {
        super(epoch, tickDuration);
    }

    /**
     * Returns the current number of ticks for the {@link DefaultTimeSourceImpl}. Note: that a 'tick' is a period defined by the timesource; this may be any valid {@link Duration}; be
     * it a millisecond, an hour, 2.5 seconds or any other value.
     *
     * @return The current number of ticks to be used by an {@link SnowflakeIdGenerator} when creating an Id.
     */
    @Override
    public long getTicks() {
        return getOffset().plus(getElapsed()).dividedBy(getTickDuration());
    }
}
