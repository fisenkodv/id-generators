package net.fisenko.utils.ids.snowflake;

import java.util.Iterator;

public class IdGeneratorIterator<T> implements Iterator<T> {
    private final IdGenerator<T> idGenerator;

    public IdGeneratorIterator(IdGenerator<T> idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration
     */
    @Override
    public T next() {
        try {
            return idGenerator.createId();
        } catch (InvalidSystemClockException | SequenceOverflowException e) {
            e.printStackTrace();
            return null;
        }
    }
}
