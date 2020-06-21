# <a href='https://github.com/fisenkodv/id-generators/blob/master/src/main/java/net/fisenko/utils/ids/snowflake/README.md'><img src="https://github.com/fisenkodv/id-generators/blob/master/assets/images/snowflake/logo.png" height="80px"/></a>

## Why

In certain situations, you need a low-latency, distributed, uncoordinated, (roughly) time ordered, compact, and highly available Id generation system. This project was inspired by [Twitter's Snowflake](https://github.com/twitter/snowflake) project which has been retired and [IdGen](https://github.com/RobThree/IdGen) which is .NET implementation.

## How it works

`SnowflakeIdGenerator` generates, like Snowflake, 64-bit Ids. The [Sign Bit](https://en.wikipedia.org/wiki/Sign_bit) is unused since this can cause incorrect ordering on some systems that cannot use unsigned types and/or make it hard to get correct ordering. So, in effect, `SnowflakeIdGenerator` generates 63-bit Ids. An Id consists of 3 parts:

* Timestamp
* Generator-id
* Sequence

An Id generated with a **Default** `MaskConfig` is structured as follows:

![Id structure](https://github.com/fisenkodv/id-generators/blob/master/assets/images/snowflake/structure.png)

However, using the `MaskConfig` class you can tune the structure of the created Id's to your own needs; you can use 45 bits for the timestamp, 2 bits for the generator-id, and 16 bits for the sequence if you prefer. As long as all 3 parts (timestamp, generator, and sequence) add up to 63 bits you're good to go!

The **timestamp**-part of the Id should speak for itself; by default, this is incremented every millisecond and represents the number of milliseconds since a certain epoch. However, `SnowflakeIdGenerator` relies on a [`TimeSource`](src/main/java/net/fisenko/utils/ids/snowflake/TimeSource.java) which uses a 'tick' that can be defined to be anything; be it a millisecond (default), a second or even a day or nanosecond (hardware support, etc. permitting). By default, `SnowflakeIdGenerator` uses 2010-01-01 0:00:00Z as an epoch, but you can specify a custom epoch too.

The **generator-id**-part of the Id is the part that you 'configure'; it could correspond to a host, thread, datacenter, or continent: it's up to you. However, the generator-id should be unique in the system: if you have several hosts or threads generating Id's, each host or thread should have its own generator-id. This could be based on the hostname, a config-file value, or even be retrieved from a coordinating service. **Remember**: a generator-id should be unique within the entire system to avoid collisions!

The **sequence**-part is simply a value that is incremented each time a new Id is generated within the same tick (again, by default, a millisecond but can be anything); it is reset every time the tick changes.

## System Clock Dependency

It's strongly recommended to use NTP to keep system clock accurate. `SnowflakeIdGenerator` protects from non-monotonic clocks, i.e. clocks that run backward.

The [`DefaultTimeSource`](src/main/java/net/fisenko/utils/ids/snowflake/DefaultTimeSource.java) relies on a [`Stopwatch`](https://commons.apache.org/proper/commons-lang/javadocs/api-release/org/apache/commons/lang3/time/StopWatch.html) for calculating the 'ticks'. A custom time source could be added by implementing the [`ITimeSource`](src/main/java/net/fisenko/utils/ids/snowflake/TimeSource.java) interface.

## Getting started

Install the [Nuget package](https://www.nuget.org/packages/IdGen) and write the following code:

```c#
import net.fisenko.utils.ids.snowflake.IdGenerator;
import net.fisenko.utils.ids.snowflake.InvalidSystemClockException;
import net.fisenko.utils.ids.snowflake.SequenceOverflowException;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGenerator;

public class App {
    public static void main(String[] args) throws InvalidSystemClockException, SequenceOverflowException {
        IdGenerator<Long> generator = new SnowflakeIdGenerator(0);
        long id = generator.createId();
        System.out.println(id);
    }
}
```

The above example creates a default `IdGenerator` with the `generatorId` (or: 'Worker Id') set to 0 and using a [`DefaultTimeSource`](src/main/java/net/fisenko/utils/ids/snowflake/DefaultTimeSource.java). If you're using multiple generators (across machines or in separate threads or...) you'll want to make sure each generator is assigned its own unique Id. One way of doing this is by simply storing a value in your configuration file, for example, another way may involve a service handing out generatorId's to machines/threads. `SnowflakeIdGenerator` **does not** provide a solution for this since each project or setup may have different requirements or infrastructure to provide these generator-id's.

The below sample is a bit more complicated; we set a custom epoch, define our own (bit)mask configuration for generated Id's and then display some information about the setup:

```java
import net.fisenko.utils.ids.snowflake.IdGenerator;
import net.fisenko.utils.ids.snowflake.InvalidSystemClockException;
import net.fisenko.utils.ids.snowflake.MaskConfig;
import net.fisenko.utils.ids.snowflake.SequenceOverflowException;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGenerator;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class App {
    public static void main(String[] args) throws InvalidSystemClockException, SequenceOverflowException {
        // June 1st 2020 is the epoch
        OffsetDateTime epoch = OffsetDateTime.of(LocalDateTime.of(2020, 6, 1, 0, 0, 0), ZoneOffset.UTC);
        // Create a mask configuration of 45 bits for timestamp, 2 for generator-id and 16 for sequence
        MaskConfig maskConfig = new MaskConfig((byte) 45, (byte) 2, (byte) 16);

        IdGenerator<Long> generator = new SnowflakeIdGenerator(0, epoch, maskConfig);

        // Let's ask the mask configuration how many generators we could instantiate in this setup (2 bits)
        System.out.printf("Max. generators: %d", maskConfig.getMaxGenerators());
        System.out.println();

        // Let's ask the mask configuration how many sequential Id's we could generate
        // in a single ms in this setup (16 bits)
        System.out.printf("Id's/ms per generator: %d", maskConfig.getMaxSequenceIds());
        System.out.println();

        // Let's calculate the number of Id's we could generate, per ms, should we use
        // the maximum number of generators
        System.out.printf("Id's/ms total: %d", maskConfig.getMaxGenerators() * maskConfig.getMaxSequenceIds());
        System.out.println();

        // Let's ask the mask configuration for how long we could generate Id's before
        // we experience a 'wraparound' of the timestamp
        System.out.printf("Wraparound interval: %s", maskConfig.getWrapAroundInterval(generator.getTimeSource()));
        System.out.println();

        // And finally: let's ask the mask configuration when this wraparound will happen
        // (we'll have to tell it the generator's epoch)
        System.out.printf("Wraparound date: %s", maskConfig.getWrapAroundDate(generator.getEpoch(), generator.getTimeSource()));
        System.out.println();
    }
}
```

Output:

```txt
Max. generators: 4
Id's/ms per generator: 65536
Id's/ms total: 262144
Wraparound interval: PT9773436H41M28.832S
Wraparound date: 3135-05-14T12:41:28.832Z
```

`SnowflakeIdGenerator` also provides a `TimeSource` interface; this can be handy for unittesting purposes or if you want to provide a time-source for the timestamp part of your Id's that is not based on the system time.

The following constructor overloads are available:

```java
SnowflakeIdGenerator(int generatorId)
SnowflakeIdGenerator(int generatorId, OffsetDateTime epoch)
SnowflakeIdGenerator(int generatorId, MaskConfig maskConfig)
SnowflakeIdGenerator(int generatorId, OffsetDateTime epoch, MaskConfig maskConfig)
SnowflakeIdGenerator(int generatorId, TimeSource timeSource)
SnowflakeIdGenerator(int generatorId, MaskConfig maskConfig, TimeSource timeSource)
```

All properties are read-only to prevent changes once an `SnowflakeIdGenerator` has been instantiated.

<hr>

Icon made by [Freepik](http://www.flaticon.com/authors/freepik) from [www.flaticon.com](http://www.flaticon.com) is licensed by [CC 3.0](http://creativecommons.org/licenses/by/3.0/).