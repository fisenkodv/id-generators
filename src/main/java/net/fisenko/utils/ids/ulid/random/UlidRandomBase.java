package net.fisenko.utils.ids.ulid.random;

/**
 * Provides a baseclass for {@link UlidRandom}
 */
public abstract class UlidRandomBase implements UlidRandom {

    /**
     * Default number of random bytes generated
     */
    protected final static int RANDOM_LENGTH = 10;

    /**
     * Creates and returns random bytes.
     *
     * @return random bytes
     */
    public abstract byte[] getRandomBytes();
}
