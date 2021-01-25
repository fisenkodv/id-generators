package net.fisenko.utils.ids.ulid.random.impl;

import java.security.SecureRandom;
import net.fisenko.utils.ids.ulid.random.UlidRandomBase;

/**
 * A cryptographically secure random for the random part of ulid's.
 */
public class CryptographicallySecureRandomImpl extends UlidRandomBase {

    // We only need one, single, instance of a random so we keep it around.
    private static final SecureRandom random = new SecureRandom();

    /**
     * Creates and returns cryptographically secure random bytes.
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