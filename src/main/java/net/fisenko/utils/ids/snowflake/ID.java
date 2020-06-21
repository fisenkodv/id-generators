package net.fisenko.utils.ids.snowflake;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Holds information about a decoded id.
 */
public class ID {
    private final int sequenceNumber;
    private final int generatorId;
    private final OffsetDateTime dateTimeOffset;

    /// <summary>
    /// Initializes a new instance of the <see cref="ID"/> struct.
    /// </summary>
    /// <param name="sequenceNumber">The sequence number of the id.</param>
    /// <param name="generatorId">The generator id of the generator that generated the id.</param>
    /// <param name="dateTimeOffset">The date/time when the id was generated.</param>
    /// <returns></returns>
    ID(int sequenceNumber, int generatorId, OffsetDateTime dateTimeOffset) {
        this.sequenceNumber = sequenceNumber;
        this.generatorId = generatorId;
        this.dateTimeOffset = dateTimeOffset;
    }

    /**
     * Gets the sequence number of the id.
     * @return Returns the sequence number.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Gets the generator id of the generator that generated the id.
     * @return Returns the generator id.
     */
    public int getGeneratorId() {
        return generatorId;
    }

    /**
     * Gets the date/time when the id was generated.
     * @return Returns date/time.
     */
    public OffsetDateTime getDateTimeOffset() {
        return dateTimeOffset;
    }

    /**
     * The hash code for this instance.
     * @return Returns the hash code for this instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(dateTimeOffset, generatorId, sequenceNumber);
    }

    /**
     * Returns a value indicating whether this instance and a specified {@link ID} object represent the same value.
     * @param obj An {@link ID} to compare to this instance.
     * @return Returns true if {@param obj} is equal to this instance; otherwise, false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ID) {
            ID other = (ID) obj;
            return generatorId == other.generatorId
                    && dateTimeOffset.equals(other.dateTimeOffset)
                    && sequenceNumber == other.sequenceNumber;
        }

        return false;
    }
}