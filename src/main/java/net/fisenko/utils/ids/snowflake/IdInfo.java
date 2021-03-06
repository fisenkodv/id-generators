package net.fisenko.utils.ids.snowflake;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Holds information about a decoded id.
 */
public class IdInfo {

    private final int sequenceNumber;
    private final int generatorId;
    private final OffsetDateTime dateTimeOffset;

    /**
     * Initializes a new instance of the {@link IdInfo} struct.
     *
     * @param sequenceNumber the sequence number of the id.
     * @param generatorId    the generator id of the generator that generated the id.
     * @param dateTimeOffset the date/time when the id was generated.
     */
    public IdInfo(int sequenceNumber, int generatorId, OffsetDateTime dateTimeOffset) {
        this.sequenceNumber = sequenceNumber;
        this.generatorId = generatorId;
        this.dateTimeOffset = dateTimeOffset;
    }

    /**
     * Gets the sequence number of the id.
     *
     * @return Returns the sequence number.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Gets the generator id of the generator that generated the id.
     *
     * @return Returns the generator id.
     */
    public int getGeneratorId() {
        return generatorId;
    }

    /**
     * Gets the date/time when the id was generated.
     *
     * @return Returns date/time.
     */
    public OffsetDateTime getDateTimeOffset() {
        return dateTimeOffset;
    }

    /**
     * The hash code for this instance.
     *
     * @return Returns the hash code for this instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(dateTimeOffset, generatorId, sequenceNumber);
    }

    /**
     * Returns a value indicating whether this instance and a specified {@link IdInfo} object represent the same value.
     *
     * @param obj An {@link IdInfo} to compare to this instance.
     * @return Returns true if {@param obj} is equal to this instance; otherwise, false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IdInfo) {
            IdInfo other = (IdInfo) obj;
            return generatorId == other.generatorId
                    && dateTimeOffset.equals(other.dateTimeOffset)
                    && sequenceNumber == other.sequenceNumber;
        }

        return false;
    }
}