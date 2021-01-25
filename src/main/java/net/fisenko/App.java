package net.fisenko;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGenerator;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGeneratorImpl;
import net.fisenko.utils.ids.snowflake.config.MaskConfig;

public class App {

    public static void main(String[] args) throws Exception {
        // June 1st 2020 is the epoch
        OffsetDateTime epoch = OffsetDateTime.of(LocalDateTime.of(2020, 6, 1, 0, 0, 0), ZoneOffset.UTC);
        // Create a mask configuration of 45 bits for timestamp, 2 for generator-id and 16 for sequence
        MaskConfig maskConfig = new MaskConfig((byte) 45, (byte) 2, (byte) 16);

        SnowflakeIdGenerator generator = new SnowflakeIdGeneratorImpl(0, epoch, maskConfig);

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
