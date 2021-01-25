package net.fisenko.utils.ids.ulid.random;

/**
 * Defines the interface for ulid Random Number Generators.
 */
public interface UlidRandom {

    /**
     * Creates and returns random bytes
     *
     * @return random bytes
     */
    byte[] getRandomBytes();
}
