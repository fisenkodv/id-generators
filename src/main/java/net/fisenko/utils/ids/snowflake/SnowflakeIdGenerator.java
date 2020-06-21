package net.fisenko.utils.ids.snowflake;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;

/**
 * Generates Id's inspired by Twitter's (late) Snowflake project.
 */
public class SnowflakeIdGenerator implements IdGenerator<Long> {
    // Returns the default epoch.
    public static final OffsetDateTime DefaultEpoch = OffsetDateTime.of(LocalDateTime.of(2010, 1, 1, 0, 0, 0), ZoneOffset.UTC);
    private final long generatorId;
    private final long MASK_SEQUENCE;
    private final long MASK_TIME;
    private final long MASK_GENERATOR;
    private final int SHIFT_TIME;
    private final int SHIFT_GENERATOR;
    private final MaskConfig maskConfig;
    // Object to lock() on while generating Id's
    private final Object lock = new Object();
    private final TimeSource timeSource;
    private int sequence = 0;
    private long lastGen = -1;

    /**
     * Initializes a new instance of the {@link IdGenerator} class, 2015-01-01 0:00:00Z is used as default
     * epoch and the {@link MaskConfig#Default} value is used for the {@link MaskConfig}. The
     * {@link DefaultTimeSource} is used to retrieve timestamp information.
     * @param generatorId The Id of the generator.
     * @throws IllegalArgumentException Thrown when either maskConfig, or timeSource is null,
     *                                  or maskConfig defines a non-63 bit bitmask,
     *                                  or GeneratorId or Sequence masks are >31 bit, GeneratorId exceeds maximum value or epoch in future.
     */
    public SnowflakeIdGenerator(int generatorId) {
        this(generatorId, DefaultEpoch);
    }

    /**
     * Initializes a new instance of the {@link IdGenerator} class. The {@link MaskConfig#Default} value is used for
     * the {@link MaskConfig}. The {@link DefaultTimeSource} is used to retrieve timestamp information.
     * @param generatorId The Id of the generator.
     * @param epoch       The Epoch of the generator.
     * @throws IllegalArgumentException Thrown when either maskConfig, or timeSource is null,
     *                                  or maskConfig defines a non-63 bit bitmask,
     *                                  or GeneratorId or Sequence masks are >31 bit, GeneratorId exceeds maximum value or epoch in future.
     */
    public SnowflakeIdGenerator(int generatorId, OffsetDateTime epoch) {
        this(generatorId, epoch, MaskConfig.Default);
    }

    /**
     * Initializes a new instance of the {@link IdGenerator} class. The {@link DefaultTimeSource} is used
     * to retrieve timestamp information.
     * @param generatorId The Id of the generator.
     * @param maskConfig  The {@link MaskConfig} of the generator.
     * @throws IllegalArgumentException Thrown when either maskConfig, or timeSource is null,
     *                                  or maskConfig defines a non-63 bit bitmask,
     *                                  or GeneratorId or Sequence masks are >31 bit, GeneratorId exceeds maximum value or epoch in future.
     */
    public SnowflakeIdGenerator(int generatorId, MaskConfig maskConfig) {
        this(generatorId, maskConfig, new DefaultTimeSource(DefaultEpoch));
    }

    /**
     * Initializes a new instance of the {@link IdGenerator} class. The {@link DefaultTimeSource} is used
     * to retrieve timestamp information.
     * @param generatorId The Id of the generator.
     * @param epoch       The epoch of the generator.
     * @param maskConfig  The {@link MaskConfig} of the generator.
     * @throws IllegalArgumentException Thrown when either maskConfig, or timeSource is null,
     *                                  or maskConfig defines a non-63 bit bitmask,
     *                                  or GeneratorId or Sequence masks are >31 bit, GeneratorId exceeds maximum value or epoch in future.
     */
    public SnowflakeIdGenerator(int generatorId, OffsetDateTime epoch, MaskConfig maskConfig) {
        this(generatorId, maskConfig, new DefaultTimeSource(epoch));
    }

    /**
     * Initializes a new instance of the {@link IdGenerator} class.
     * @param generatorId The Id of the generator.
     * @param timeSource  The time-source to use when acquiring time data.
     * @throws IllegalArgumentException Thrown when either maskConfig, or timeSource is null,
     *                                  or maskConfig defines a non-63 bit bitmask,
     *                                  or GeneratorId or Sequence masks are >31 bit, GeneratorId exceeds maximum value or epoch in future.
     */
    public SnowflakeIdGenerator(int generatorId, TimeSource timeSource) {
        this(generatorId, MaskConfig.Default, timeSource);
    }

    /**
     * Initializes a new instance of the {@link IdGenerator} class.
     * @param generatorId The Id of the generator.
     * @param maskConfig  The {@link MaskConfig} of the generator.
     * @param timeSource  The time-source to use when acquiring time data.
     * @throws IllegalArgumentException Thrown when either maskConfig, or timeSource is null,
     *                                  or maskConfig defines a non-63 bit bitmask,
     *                                  or GeneratorId or Sequence masks are >31 bit, GeneratorId exceeds maximum value or epoch in future.
     */
    public SnowflakeIdGenerator(int generatorId, MaskConfig maskConfig, TimeSource timeSource) {
        if (maskConfig == null) {
            throw new IllegalArgumentException("'maskConfig' could not be null.");
        }

        if (timeSource == null) {
            throw new IllegalArgumentException("'timeSource' could not be null.");
        }

        if (maskConfig.getTotalBits() != 63) {
            throw new IllegalArgumentException("Number of bits used to generate Id's is not equal to 63");
        }

        if (maskConfig.getGeneratorIdBits() > 31) {
            throw new IllegalArgumentException("GeneratorId cannot have more than 31 bits");
        }

        if (maskConfig.getSequenceBits() > 31) {
            throw new IllegalArgumentException("Sequence cannot have more than 31 bits");
        }

        // Pre-calculate some values
        MASK_TIME = getMask(maskConfig.getTimestampBits());
        MASK_GENERATOR = getMask(maskConfig.getGeneratorIdBits());
        MASK_SEQUENCE = getMask(maskConfig.getSequenceBits());

        if (generatorId < 0 || generatorId > MASK_GENERATOR) {
            throw new IllegalArgumentException(String.format("GeneratorId must be between 0 and %d (inclusive).", MASK_GENERATOR));
        }

        SHIFT_TIME = maskConfig.getGeneratorIdBits() + maskConfig.getSequenceBits();
        SHIFT_GENERATOR = maskConfig.getSequenceBits();

        // Store instance specific values
        this.maskConfig = maskConfig;
        this.timeSource = timeSource;
        this.generatorId = generatorId;
    }

    /**
     * Gets the Id of the generator.
     * @return Returns id/
     */
    public int getId() {
        return (int) generatorId;
    }

    /**
     * Creates a new Id.
     * @return Returns an Id based on the {@link IdGenerator}'s epoch, generatorid and sequence.
     * @throws InvalidSystemClockException Thrown when clock going backwards is detected.
     * @throws SequenceOverflowException   Thrown when sequence overflows.
     */
    public Long createId() throws InvalidSystemClockException, SequenceOverflowException {
        return createIdImpl();
    }

    /**
     * Gets the {@link TimeSource} for the {@link IdGenerator}.
     * @return Returns time source.
     */
    public TimeSource getTimeSource() {
        return timeSource;
    }

    /**
     * Gets the epoch for the {@link IdGenerator}.
     * @return Returns offset.
     */
    public OffsetDateTime getEpoch() {
        return getTimeSource().getEpoch();
    }

    /**
     * Gets the {@link MaskConfig} for the {@link IdGenerator}.
     * @return Returns mask config.
     */
    public MaskConfig getMaskConfig() {
        return maskConfig;
    }

    /**
     * Returns information about an Id such as the sequence number, generator id and date/time the Id was generated
     * based on the current mask config of the generator.
     * IMPORTANT: note that this method relies on the mask config and timesource; if the id was generated with a
     * different mask config and/or timesource than the current one the 'decoded' ID will NOT contain correct
     * information.
     * @param id The Id to extract information from.
     * @return Returns an {@link ID} that contains information about the 'decoded' Id.
     */
    public ID fromId(long id) {
        // Deconstruct Id by unshifting the bits into the proper parts
        return new ID(
                (int) (id & MASK_SEQUENCE),
                (int) ((id >> SHIFT_GENERATOR) & MASK_GENERATOR),
                timeSource.getEpoch().plus(Duration.ofNanos(((id >> SHIFT_TIME) & MASK_TIME) * timeSource.getTickDuration().getNano()))
        );
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     * @return an Iterator.
     */
    @Override
    public Iterator<Long> iterator() {
        return new IdGeneratorIterator<>(this);
    }

    /**
     * Gets a bitmask masking out the desired number of bits; a bitmask of 2 returns 000...000011,
     * a bitmask of 5 returns 000...011111.
     * @param bits The number of bits to mask.
     * @return Returns the desired bitmask.
     */
    private long getMask(byte bits) {
        return (1L << bits) - 1;
    }

    /**
     * Creates a new Id.
     * @return Returns an Id based on the {@link IdGenerator}'s epoch, generatorid and sequence or a negative value
     * when an exception occurred.
     * @throws InvalidSystemClockException Thrown when clock going backwards is detected.
     * @throws SequenceOverflowException   Thrown when sequence overflows.
     */
    private long createIdImpl() throws InvalidSystemClockException, SequenceOverflowException {
        synchronized (lock) {
            // Determine "timeslot" and make sure it's >= last timeslot (if any)
            long ticks = getTicks();
            long timestamp = ticks & MASK_TIME;

            if (timestamp < lastGen || ticks < 0) {
                throw new InvalidSystemClockException(String.format("Clock moved backwards or wrapped around. Refusing to generate id for %d ticks", lastGen - timestamp));
            }

            // If we're in the same "timeslot" as previous time we generated an Id, up the sequence number
            if (timestamp == lastGen) {
                if (sequence >= MASK_SEQUENCE) {
                    throw new SequenceOverflowException("Sequence overflow. Refusing to generate id for rest of tick");
                }
                sequence++;
            } else { // If we're in a new(er) "timeslot", so we can reset the sequence and store the new(er) "timeslot"
                sequence = 0;
                lastGen = timestamp;
            }

            // Build id by shifting all bits into their place
            return (timestamp << SHIFT_TIME)
                    + (generatorId << SHIFT_GENERATOR)
                    + sequence;
        }
    }

    /**
     * Gets the number of ticks since the {@link TimeSource}'s epoch.
     * @return Returns the number of ticks since the {@link TimeSource}'s epoch.
     */
    private long getTicks() {
        return timeSource.getTicks();
    }
}
