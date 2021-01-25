package net.fisenko.utils.ids.ulid;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import net.fisenko.utils.ids.ulid.random.UlidRandom;
import net.fisenko.utils.ids.ulid.random.impl.MonotonicUlidRandomImpl;

public class UlidIdGeneratorImpl implements UlidIdGenerator {

    private final UlidRandom rng;

    private final OffsetDateTime time;

    /**
     * Creates and returns a new {@link UlidIdGeneratorImpl} based on the specified time and using the specified random.
     *
     * @param time the {@link OffsetDateTime} to use for the time-part of the {@link UlidIdGeneratorImpl}.
     * @param rng  the {@link UlidRandom} to use for random number generation.
     * @throws IllegalArgumentException when {@code rnd} is null.
     */
    public UlidIdGeneratorImpl(OffsetDateTime time, UlidRandom rng) {
        if (rng == null) {
            throw new IllegalArgumentException("'rng' could not be null.");
        }
        this.time = time;
        this.rng = rng;
    }

    public UlidIdGeneratorImpl() {
        this(OffsetDateTime.now(ZoneOffset.UTC), new MonotonicUlidRandomImpl());
    }

    @Override
    public Ulid next() throws Exception {
        return new Ulid(this.time, this.rng.getRandomBytes());
    }
}
