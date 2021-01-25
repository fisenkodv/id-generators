package net.fisenko.utils.ids.ulid.utils;

public class Base32 {

    /**
     * Char to index lookup array for massive speedup since we can find a char's index in O(1). We use 255 as 'sentinel' value for invalid indexes.
     */
    public final static byte[] C2B32 = new byte[]{
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255,
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255,
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255,
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255,
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 0, (byte) 1,
            (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 255, (byte) 255, (byte) 255,
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15,
            (byte) 16, (byte) 17, (byte) 1, (byte) 18, (byte) 19, (byte) 1, (byte) 20, (byte) 21, (byte) 0, (byte) 22, (byte) 23,
            (byte) 24, (byte) 25, (byte) 26, (byte) 255, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, (byte) 255, (byte) 255,
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16,
            (byte) 17, (byte) 1, (byte) 18, (byte) 19, (byte) 1, (byte) 20, (byte) 21, (byte) 0, (byte) 22, (byte) 23, (byte) 24,
            (byte) 25, (byte) 26, (byte) 255, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31
    };
    public final static int C2B32LEN = C2B32.length;
    /**
     * Base32 "alphabet"
     */
    private final static String BASE32 = "0123456789ABCDEFGHJKMNPQRSTVWXYZ";

    public static String toBase32(byte[] value) {
        // Hand-optimized unrolled loops ahead
        switch (value.length) {
            case 6:     // Time part
                return new String(
                        new char[]{
                                /* 0  */ BASE32.charAt((value[0] & 224) >> 5),                             /* 1  */ BASE32.charAt(value[0] & 31),
                                /* 2  */ BASE32.charAt((value[1] & 248) >> 3),                             /* 3  */ BASE32.charAt(((value[1] & 7) << 2) | ((value[2] & 192) >> 6)),
                                /* 4  */ BASE32.charAt((value[2] & 62) >> 1),                              /* 5  */ BASE32.charAt(((value[2] & 1) << 4) | ((value[3] & 240) >> 4)),
                                /* 6  */ BASE32.charAt(((value[3] & 15) << 1) | ((value[4] & 128) >> 7)),  /* 7  */ BASE32.charAt((value[4] & 124) >> 2),
                                /* 8  */ BASE32.charAt(((value[4] & 3) << 3) | ((value[5] & 224) >> 5)),   /* 9  */ BASE32.charAt(value[5] & 31),
                        }
                );
            case 10:    // Random part
                return new String(
                        new char[]{
                                /* 0  */ BASE32.charAt((value[0] & 248) >> 3),                             /* 1  */ BASE32.charAt(((value[0] & 7) << 2) | ((value[1] & 192) >> 6)),
                                /* 2  */ BASE32.charAt((value[1] & 62) >> 1),                              /* 3  */ BASE32.charAt(((value[1] & 1) << 4) | ((value[2] & 240) >> 4)),
                                /* 4  */ BASE32.charAt(((value[2] & 15) << 1) | ((value[3] & 128) >> 7)),  /* 5  */ BASE32.charAt((value[3] & 124) >> 2),
                                /* 6  */ BASE32.charAt(((value[3] & 3) << 3) | ((value[4] & 224) >> 5)),   /* 7  */ BASE32.charAt(value[4] & 31),
                                /* 8  */ BASE32.charAt((value[5] & 248) >> 3),                             /* 9  */ BASE32.charAt(((value[5] & 7) << 2) | ((value[6] & 192) >> 6)),
                                /* 10 */ BASE32.charAt((value[6] & 62) >> 1),                              /* 11 */ BASE32.charAt(((value[6] & 1) << 4) | ((value[7] & 240) >> 4)),
                                /* 12 */ BASE32.charAt(((value[7] & 15) << 1) | ((value[8] & 128) >> 7)),  /* 13 */ BASE32.charAt((value[8] & 124) >> 2),
                                /* 14 */ BASE32.charAt(((value[8] & 3) << 3) | ((value[9] & 224) >> 5)),   /* 15 */ BASE32.charAt(value[9] & 31),
                        }
                );
        }
        throw new IllegalStateException("Invalid length");
    }

    public static byte[] fromBase32(String v) {
        // Hand-optimized unrolled loops ahead
        switch (v.length()) {
            case 10:    // Time part
                return new byte[]
                        {
                                /* 0 */ (byte) ((C2B32[v.charAt(0)] << 5) | C2B32[v.charAt(1)]),                                       /* 1 */
                                (byte) ((C2B32[v.charAt(2)] << 3) | (C2B32[v.charAt(3)] >> 2)),
                                /* 2 */ (byte) ((C2B32[v.charAt(3)] << 6) | (C2B32[v.charAt(4)] << 1) | (C2B32[v.charAt(5)] >> 4)),    /* 3 */
                                (byte) ((C2B32[v.charAt(5)] << 4) | (C2B32[v.charAt(6)] >> 1)),
                                /* 4 */ (byte) ((C2B32[v.charAt(6)] << 7) | (C2B32[v.charAt(7)] << 2) | (C2B32[v.charAt(8)] >> 3)),    /* 5 */
                                (byte) ((C2B32[v.charAt(8)] << 5) | C2B32[v.charAt(9)]),
                        };
            case 16:    // Random part
                return new byte[]
                        {
                                /* 0 */ (byte) ((C2B32[v.charAt(0)] << 3) | (C2B32[v.charAt(1)] >> 2)),                                /* 1 */
                                (byte) ((C2B32[v.charAt(1)] << 6) | (C2B32[v.charAt(2)] << 1) | (C2B32[v.charAt(3)] >> 4)),
                                /* 2 */ (byte) ((C2B32[v.charAt(3)] << 4) | (C2B32[v.charAt(4)] >> 1)),                                /* 3 */
                                (byte) ((C2B32[v.charAt(4)] << 7) | (C2B32[v.charAt(5)] << 2) | (C2B32[v.charAt(6)] >> 3)),
                                /* 4 */ (byte) ((C2B32[v.charAt(6)] << 5) | C2B32[v.charAt(7)]),                                       /* 5 */
                                (byte) ((C2B32[v.charAt(8)] << 3) | C2B32[v.charAt(9)] >> 2),
                                /* 6 */ (byte) ((C2B32[v.charAt(9)] << 6) | (C2B32[v.charAt(10)] << 1) | (C2B32[v.charAt(11)] >> 4)),  /* 7 */
                                (byte) ((C2B32[v.charAt(11)] << 4) | (C2B32[v.charAt(12)] >> 1)),
                                /* 8 */ (byte) ((C2B32[v.charAt(12)] << 7) | (C2B32[v.charAt(13)] << 2) | (C2B32[v.charAt(14)] >> 3)), /* 9 */
                                (byte) ((C2B32[v.charAt(14)] << 5) | C2B32[v.charAt(15)]),
                        };
        }
        throw new IllegalStateException("Invalid length");
    }
}
