package net.fisenko.utils.ids.ulid.random.impl;

import java.util.Random;
import net.fisenko.utils.ids.ulid.random.UlidRandomBase;

/**
 * A simple (but fast(er)) random for the random part of ulid's.
 */
public class SimpleUlidRandomImpl extends UlidRandomBase {

    // We only need one, single, instance of a random so we keep it around.
    private static final Random random = new Random();

    /**
     * Creates and returns random bytes
     *
     * @return random bytes
     */
    @Override
    public byte[] getRandomBytes() {
        byte[] buffer = new byte[RANDOM_LENGTH];
        random.nextBytes(buffer);
        return buffer;
    }
}