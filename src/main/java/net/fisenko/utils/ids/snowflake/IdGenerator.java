package net.fisenko.utils.ids.snowflake;

import java.time.OffsetDateTime;
import java.util.Iterator;

/**
 * Provides the interface for Id-generators.
 * @param <T> The type for the generated ID's.
 */
public interface IdGenerator<T> extends Iterable<T> {
    /**
     * Creates a new Id.
     * @return Returns an Id.
     */
    T createId() throws InvalidSystemClockException, SequenceOverflowException;

    /**
     * Gets the {@link TimeSource} for the {@link IdGenerator}
     * @return Returns time source.
     */
    TimeSource getTimeSource();

    /**
     * Gets the epoch for the {@link IdGenerator}
     * @return Returns date time offset.
     */
    OffsetDateTime getEpoch();

    /**
     * Gets the {@link MaskConfig} for the {@link IdGenerator}
     * @return Returns mask config.
     */
    MaskConfig getMaskConfig();
}
