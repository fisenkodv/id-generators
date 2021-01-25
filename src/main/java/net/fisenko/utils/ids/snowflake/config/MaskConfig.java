package net.fisenko.utils.ids.snowflake.config;

import java.time.Duration;
import java.time.OffsetDateTime;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGenerator;
import net.fisenko.utils.ids.snowflake.timeSource.TimeSource;

/**
 * Specifies the number of bits to use for the different parts of an Id for an {@link SnowflakeIdGenerator}.
 */
public class MaskConfig {

    /**
     * Gets a default {@link MaskConfig} with 41 bits for the timestamp part, 10 bits for the generator-id part and 12 bits for the sequence part of the id.
     */
    public static final MaskConfig Default = new MaskConfig((byte) 41, (byte) 10, (byte) 12);
    private final byte timestampBits;
    private final byte generatorIdBits;
    private final byte sequenceBits;

    /**
     * Initializes a bitmask configuration for {@link SnowflakeIdGenerator}.
     *
     * @param timestampBits   Number of bits to use for the timestamp-part of Id's.
     * @param generatorIdBits Number of bits to use for the generator-id of Id's.
     * @param sequenceBits    Number of bits to use for the sequence-part of Id's.
     */
    public MaskConfig(byte timestampBits, byte generatorIdBits, byte sequenceBits) {
        this.timestampBits = timestampBits;
        this.generatorIdBits = generatorIdBits;
        this.sequenceBits = sequenceBits;
    }

    /**
     * Gets number of bits to use for the timestamp part of the Id's to generate.
     *
     * @return Returns number of bits.
     */
    public byte getTimestampBits() {
        return timestampBits;
    }

    /**
     * Gets number of bits to use for the generator-id part of the Id's to generate.
     *
     * @return Returns number of bits.
     */
    public byte getGeneratorIdBits() {
        return generatorIdBits;
    }

    /**
     * Gets number of bits to use for the sequence part of the Id's to generate.
     *
     * @return Returns number of bits.
     */
    public byte getSequenceBits() {
        return sequenceBits;
    }

    /**
     * Gets the total number of bits for the {@link MaskConfig}.
     *
     * @return Returns total number of bits.
     */
    public int getTotalBits() {
        return timestampBits + generatorIdBits + sequenceBits;
    }

    /**
     * Gets the maximum number of intervals for this mask configuration.
     *
     * @return Returns the maximum number of intervals.
     */
    public long getMaxIntervals() {
        return (1L << timestampBits);
    }

    /**
     * Gets the maximum number of generators available for this mask configuration.
     *
     * @return Returns the maximum number of generators.
     */
    public long getMaxGenerators() {
        return (1L << generatorIdBits);
    }

    /**
     * Gets the maximum number of sequential Id's for a time-interval (e.g. max. number of Id's generated within a single interval).
     *
     * @return Returns the maximum number of Id's/
     */
    public long getMaxSequenceIds() {
        return (1L << sequenceBits);
    }

    /**
     * Calculates the last date for an Id before a 'wrap around' will occur in the timestamp-part of an Id for the given {@link MaskConfig}.
     *
     * @param epoch      The used epoch for the {@link SnowflakeIdGenerator} to use as offset.
     * @param timeSource The used {@link TimeSource} for the {@link SnowflakeIdGenerator}.
     * @return The last date for an Id before a 'wrap around' will occur in the timestamp-part of an Id.
     * @throws IllegalArgumentException Thrown when 'timeSource' is null.
     */
    public OffsetDateTime getWrapAroundDate(OffsetDateTime epoch, TimeSource timeSource) {
        if (timeSource == null) {
            throw new IllegalArgumentException("'timeSource' could not be null.");
        }
        return epoch.plus(timeSource.getTickDuration().multipliedBy(getMaxIntervals()));
    }

    /**
     * Calculates the interval at which a 'wrap around' will occur in the timestamp-part of an Id for the given {@link MaskConfig}.
     *
     * @param timeSource The used {@link TimeSource} for the {@link SnowflakeIdGenerator}.
     * @return The interval at which a 'wrap around' will occur in the timestamp-part of an Id for the given {@link MaskConfig}.
     * @throws IllegalArgumentException Thrown when 'timeSource' is null.
     */
    public Duration getWrapAroundInterval(TimeSource timeSource) {
        if (timeSource == null) {
            throw new IllegalArgumentException("'timeSource' could not be null.");
        }
        return timeSource.getTickDuration().multipliedBy(getMaxIntervals());
    }
}