# <a href='https://github.com/fisenkodv/id-generators/blob/master/src/main/java/net/fisenko/utils/ids/snowflake/README.md'><img src="https://github.com/fisenkodv/id-generators/blob/master/assets/images/ulid/logo.png" height="80px"/></a>

## Universally Unique Lexicographically Sortable Identifier

A GUID/UUID can be suboptimal for many use-cases because:

- It isn't the most character efficient way of encoding 128 bits
- It provides no other information than randomness

A ULID however:

- Is compatible with UUID/GUID's
- 1.21e+24 unique ULIDs per millisecond (1,208,925,819,614,629,174,706,176 to be exact)
- Lexicographically sortable
- Canonically encoded as a 26 character string, as opposed to the 36 character UUID
- Uses Crockford's base32 for better efficiency and readability (5 bits per character)
- Case insensitive
- No special characters (URL safe)

### Usage

Creating a ULID:

```java
// Create a ULID
UlidIdGenerator ulidIdGenerator=new UlidIdGeneratorImpl();
Ulid ulid=ulidIdGenerator.next();
System.out.println(ulidIdGenerator.next());
```

Output:

`7RTN7KEXR1BH67T1R2MPR720SD`

Parsing a ULID:

```java
// Parse ULID:
Ulid ulid=Ulid.parse("7RTN7KEXR1BH67T1R2MPR720SD");
// Print time-part of ULID:
        System.out.println(myulid.getTimestamp());
```

Output:

`2021-01-25T02:11:55Z`

## Specification

Below is the current specification of ULID as implemented in this repository.

```
 01AN4Z07BY      79KA1307SR9X4MV3
|----------|    |----------------|
 Timestamp          Randomness
  10 chars           16 chars
   48bits             80bits
   base32             base32
```

### Components

**Timestamp**

- 48 bit integer
- UNIX-time in milliseconds

**Randomness**

- 80 (Whenever possible: Cryptographically secure) Random bits

### Monotonicity

When generating a ULID within the same millisecond, it is possible to provide some guarantees regarding sort order (with some caveats). When you use the `MonotonicUlidRandomImpl`
and a newly generated ULID in the same millisecond is detected, the random component is incremented by 1 bit in the least significant bit position (with carrying). For example:

```java
// Create monotonic rng
UlidIdGenerator ulidIdGenerator=new UlidIdGeneratorImpl();
System.out.println(ulidIdGenerator.next()); //38D983EXR1EXZSJFPQ7W192JAG
System.out.println(ulidIdGenerator.next()); //38D983EXR1FBQSY3XQWQ4CJZDT
System.out.println(ulidIdGenerator.next()); //38D983EXR1FBQSY3XQWQ4CJZDV
System.out.println(ulidIdGenerator.next()); //38D983EXR1FBQSY3XQWQ4CJZDW
```

By default the most significant bit of the random part is set to zero; this ensures you can generate enough ULID's after the initial one before causing an overflow. Some
implementations simply pick a random value for the random part and increment this value, however, there's a (very small) chance that this random part is close to the overflow
value. If you then happen to generate a lot of ULID's within the same millisecond there is a risk you hit the overflow. By our method we ensure there's enough 'room' for new values
before 'running out of values' (overflowing). It is, with some effort, even possible to 'resume counting' from any given ULID.

### Encoding

[Crockford's Base32](http://www.crockford.com/wrmg/base32.html) is used as shown. This alphabet excludes the letters I, L, O, and U to avoid confusion and abuse.

```
01EWVFT6J83FZ199J5NFEQQ3S3
```

### Binary Layout and Byte Order

The components are encoded as 16 octets. Each component is encoded with the Most Significant Byte first (network byte order).

```
0                   1                   2                   3
 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
|                      32_bit_uint_time_high                    |
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
|     16_bit_uint_time_low      |       16_bit_uint_random      |
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
|                       32_bit_uint_random                      |
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
|                       32_bit_uint_random                      |
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
```

### String Representation

```
ttttttttttrrrrrrrrrrrrrrrr
```

Where:
`t` is Timestamp
`r` is Randomness
