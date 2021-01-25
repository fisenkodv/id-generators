package net.fisenko.utils.ids;

/**
 * Provides the interface for Id generators.
 *
 * @param <T> the type for the generated Id's.
 */
public interface IdGenerator<T> {

    /**
     * Creates a new Id
     *
     * @return a new Id
     */
    T next() throws Exception;
}
