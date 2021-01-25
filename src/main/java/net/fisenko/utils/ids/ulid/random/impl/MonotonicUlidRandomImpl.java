package net.fisenko.utils.ids.ulid.random.impl;

import java.security.SecureRandom;
import net.fisenko.utils.ids.ulid.Ulid;
import net.fisenko.utils.ids.ulid.UlidIdGeneratorImpl;
import net.fisenko.utils.ids.ulid.random.UlidRandom;
import net.fisenko.utils.ids.ulid.random.UlidRandomBase;
import net.fisenko.utils.ids.utils.Time;

/**
 * A random that increments the random part by 1 bit each time a random value is requested within the same millisecond.
 * <p>
 * This 'wrapper' random generates random bytes based on a specified random (which can be any random that implements {@link UlidIdGeneratorImpl}. However, when {@link
 * #getRandomBytes()} is called within the same millisecond, the last generated value + 1 will be returned instead. This causes "monotonic increasing" values.
 * </p>
 * <p>
 * To ensure there are enough values *within* the same millisecond the generated initial random value for a given millisecond will have a specified of (most significant) bits set
 * to 0 to help prevent an overflow. The default number of (most significant) bits that are set to 0 is 10, but this value can be specified and be anything between 0 and 70 where 0
 * has the highest risk of an initial random value being generated close to an overflow and 70 the lowest.
 * </p>
 */
public class MonotonicUlidRandomImpl extends UlidRandomBase {

    /**
     * Internal random to base initial values for the current millisecond on
     */
    private final SecureRandom random;

    /**
     * Object to lock on while generating
     */
    private final Object lock = new Object();
    /**
     * Contains the last generated value
     */
    private final byte[] lastValue;
    /**
     * Contains the timestamp of when the {@link #getRandomBytes()} method was last called
     */
    private long lastGen;

    /**
     * Initializes a new instance of the {@link MonotonicUlidRandomImpl} class with a default {@link UlidIdGeneratorImpl}
     */
    public MonotonicUlidRandomImpl() {
        this(null);
    }

    /**
     * Initializes a new instance of the {@link MonotonicUlidRandomImpl}
     *
     * @param lastValue the last value to 'continue from'; use null for defaults
     */
    public MonotonicUlidRandomImpl(Ulid lastValue) {
        random = new SecureRandom();
        this.lastValue = lastValue == null ? new byte[RANDOM_LENGTH] : lastValue.getRandomness();
        lastGen = Time.toUnixTimeMilli(lastValue == null ? Time.UNIX_EPOCH : lastValue.getTimestamp());
    }

    /**
     * Creates and returns random bytes based on internal {@link UlidRandom}
     * <p>
     * uses {@code System.currentTimeMillis()} an offset for which the random bytes need to be generated; this value is used to determine whether a sequence needs to be incremented
     * (same timestamp with millisecond resolution) or reset to a new random value.
     *
     * @return random bytes
     * @throws IndexOutOfBoundsException when the specified {@code dateTime} is before the last time this method was called.
     */
    @Override
    public byte[] getRandomBytes() {
        synchronized (lock) {
            // Get unix time for given datetime
            long timestamp = System.currentTimeMillis();

            // Same or earlier timestamp as last time we generated random values?
            if (timestamp <= lastGen) {
                // Increment our random value by one.
                var i = RANDOM_LENGTH;
                while (--i >= 0 && ++lastValue[i] == 0) {
                }
                // If I made it all the way to -1 we have an overflow and we throw
                if (i < 0) {
                    throw new IndexOutOfBoundsException();
                }
            } else // New(er) timestamp, so generate a new random value and store the new(er) timestamp
            {
                random.nextBytes(lastValue);
                lastValue[0] = (byte) (lastValue[0] & 0x7F);// Mask out bit 0 of the random part

                lastGen = timestamp;   // Store last timestamp
            }
            return lastValue;
        }
    }
}