package net.fisenko.utils.ids.snowflake;

import java.time.OffsetDateTime;
import net.fisenko.utils.ids.IdGenerator;
import net.fisenko.utils.ids.snowflake.config.MaskConfig;
import net.fisenko.utils.ids.snowflake.timeSource.TimeSource;

/**
 * Provides the interface for Snowflake Id generator
 */
public interface SnowflakeIdGenerator extends IdGenerator<Long> {

    /**
     * Returns the {@link TimeSource} for the {@link SnowflakeIdGenerator}.
     *
     * @return time source.
     */
    TimeSource getTimeSource();

    /**
     * Returns the epoch for the {@link SnowflakeIdGenerator}.
     *
     * @return date time offset.
     */
    OffsetDateTime getEpoch();

    /**
     * Returns the {@link MaskConfig} for the {@link SnowflakeIdGenerator}.
     *
     * @return mask config.
     */
    MaskConfig getMaskConfig();
}
