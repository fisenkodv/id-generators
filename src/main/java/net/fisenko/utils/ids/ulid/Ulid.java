package net.fisenko.utils.ids.ulid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.OffsetDateTime;
import java.util.Arrays;
import net.fisenko.utils.ids.ulid.utils.Base32;
import net.fisenko.utils.ids.utils.Time;

public class Ulid implements Comparable<Ulid> {

//    public final static Ulid MIN_VALUE = new Ulid(Time.UNIX_EPOCH, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
//
//    public final static Ulid MAX_VALUE = new Ulid(OffsetDateTime.MAX,
//            new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255});
    /**
     * Internal parts of ULID
     */
    //@formatter:off
    private final byte _a; private final byte _b; private final byte _c; private final byte _d;
    private final byte _e; private final byte _f; private final byte _g; private final byte _h;
    private final byte _i; private final byte _j; private final byte _k; private final byte _l;
    private final byte _m; private final byte _n; private final byte _o; private final byte _p;
    //@formatter:on

    /**
     * Initializes a new instance of the {@link Ulid} by using the specified array of bytes.
     *
     * @param bytes a 16-element byte array containing values with which to initialize the {@link Ulid}.
     * @throws IllegalArgumentException when {@code bytes} is null or when {@code bytes} is anything but 16 bytes long.
     */
    public Ulid(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("'bytes' could not be null.");
        }
        if (bytes.length != 16) {
            throw new IllegalArgumentException("An array of 16 elements is required");
        }

        //@formatter:off
        _a = bytes[0]; _b = bytes[1]; _c = bytes[2]; _d = bytes[3];
        _e = bytes[4]; _f = bytes[5]; _g = bytes[6]; _h = bytes[7];
        _i = bytes[8]; _j = bytes[9]; _k = bytes[10]; _l = bytes[11];
        _m = bytes[12]; _n = bytes[13]; _o = bytes[14]; _p = bytes[15];
        //@formatter:on
    }

    /**
     * Initializes a new instance of the {@link Ulid}.
     *
     * @param timePart   an offset of the {@link Ulid}.
     * @param randomPart the random part of the {@link Ulid}.
     * @throws IllegalArgumentException when {@code timePart} is before {@code Time.UNIX_EPOCH} or when the {@code randomPart} length is not equal to 10.
     */
    public Ulid(OffsetDateTime timePart, byte[] randomPart) {
        if (timePart.isBefore(Time.UNIX_EPOCH)) {
            throw new IllegalArgumentException("timePart is before " + Time.UNIX_EPOCH);
        }
        if (randomPart.length != 10) {
            throw new IllegalArgumentException("randomPart must be 10 bytes");
        }

        byte[] d = offsetDateTimeToByteArray(timePart);
        //@formatter:off
        _a = d[0]; _b = d[1]; _c = d[2]; _d = d[3]; _e = d[4]; _f = d[5];
        _g = randomPart[0]; _h = randomPart[1]; _i = randomPart[2]; _j = randomPart[3]; _k = randomPart[4];
        _l = randomPart[5]; _m = randomPart[6]; _n = randomPart[7]; _o = randomPart[8]; _p = randomPart[9];
        //@formatter:on
    }

    /**
     * Converts the string representation of a {@link Ulid} equivalent.
     *
     * @param s a string containing a {@link Ulid} to convert.
     * @return a {@link Ulid} equivalent to the value contained in {@code s}.
     * @throws IllegalArgumentException when {@code s} is null or empty or {@code s} is not in the correct format.
     */
    public static Ulid parse(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("'s' is null or empty");
        }

        String stripped = s.replace("-", "");
        if (stripped.length() != 26) {
            throw new IllegalArgumentException("Invalid Base32 string");
        }
        // Check if all chars are allowed by doing a lookup for each and seeing if we have an index < 32 for it
        for (var i = 0; i < 26; i++) {
            if (stripped.charAt(i) >= Base32.C2B32LEN || Base32.C2B32[stripped.charAt(i)] > 31) {
                throw new IllegalArgumentException("Invalid Base32 string");
            }
        }

        return new Ulid(byteArrayToOffsetDateTime(Base32.fromBase32(stripped.substring(0, 10))), Base32.fromBase32(stripped.substring(10, 26)));
    }

    private static OffsetDateTime byteArrayToOffsetDateTime(byte... value) {
        byte[] tmp = new byte[]{value[0], value[1], value[2], value[3], value[4], value[5], 0, 0};  // Pad with 2 "lost" bytes

        ByteBuffer byteBuffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
        byteBuffer.put(tmp);
        byteBuffer.flip();
        long milli = byteBuffer.getLong();

        return Time.fromUnixTimeMilli(milli);
    }

    private static byte[] offsetDateTimeToByteArray(OffsetDateTime value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
        byteBuffer.putLong(Time.toUnixTimeMilli(value));
        return byteBuffer.array();
    }

    /**
     * Gets the "time part" of the {@link Ulid}
     *
     * @return date time offset.
     */
    public OffsetDateTime getTimestamp() {
        return byteArrayToOffsetDateTime(_a, _b, _c, _d, _e, _f);
    }

    /**
     * Gets the "random part" of the {@link UlidIdGeneratorImpl}.
     *
     * @return random bytes.
     */
    public byte[] getRandomness() {
        return new byte[]{_g, _h, _i, _j, _k, _l, _m, _n, _o, _p};
    }

    /**
     * Returns a 16-element byte array that contains the value of this instance.
     *
     * @return a 16-element byte array.
     */
    public byte[] toByteArray() {
        return new byte[]{_a, _b, _c, _d, _e, _f, _g, _h, _i, _j, _k, _l, _m, _n, _o, _p};
    }

    /**
     * Compares this instance to a specified {@link Ulid} object and returns an indication of their relative
     *
     * @param ulid a {@link Ulid} to compare to this instance.
     * @return a signed number indicating the relative values of this instance and other. A negative integer if this instance is less than other, zero if this instance is equal to
     * other, a positive integer if this instance is greater than other.
     */
    @Override
    public int compareTo(Ulid ulid) {
        byte[] d = ulid.toByteArray();

        //@formatter:off
        if (_a != d[0]) return  Byte.compare(_a, d[0]); if (_b != d[1]) return Byte.compare(_b, d[1]);
        if (_c != d[2]) return Byte.compare(_c, d[2]); if (_d != d[3]) return Byte.compare(_d, d[3]);
        if (_e != d[4]) return Byte.compare(_e, d[4]); if (_f != d[5]) return Byte.compare(_f, d[5]);
        if (_g != d[6]) return Byte.compare(_g, d[6]); if (_h != d[7]) return Byte.compare(_h, d[7]);
        if (_i != d[8]) return Byte.compare(_i, d[8]); if (_j != d[9]) return Byte.compare(_j, d[9]);
        if (_k != d[10]) return Byte.compare(_k, d[10]); if (_l != d[11]) return Byte.compare(_l, d[11]);
        if (_m != d[12]) return Byte.compare(_m, d[12]); if (_n != d[13]) return Byte.compare(_n, d[13]);
        if (_o != d[14]) return Byte.compare(_o, d[14]); if (_p != d[15]) return Byte.compare(_p, d[15]);
        //@formatter:on

        return 0;
    }

    @Override
    public int hashCode() {
        byte[] bytes = toByteArray();
        return Arrays.hashCode(bytes);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof Ulid)) {
            return false;
        }
        Ulid ulid = (Ulid) that;

        byte[] a = toByteArray();
        byte[] b = ulid.toByteArray();

        for (var i = 0; i < 16; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return Base32.toBase32(new byte[]{_a, _b, _c, _d, _e, _f}) + Base32.toBase32(new byte[]{_g, _h, _i, _j, _k, _l, _m, _n, _o, _p});
    }
}
