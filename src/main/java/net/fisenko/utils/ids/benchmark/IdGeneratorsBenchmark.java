package net.fisenko.utils.ids.benchmark;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGenerator;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGeneratorImpl;
import net.fisenko.utils.ids.ulid.Ulid;
import net.fisenko.utils.ids.ulid.UlidIdGenerator;
import net.fisenko.utils.ids.ulid.UlidIdGeneratorImpl;
import net.fisenko.utils.ids.ulid.random.impl.CryptographicallySecureRandomImpl;
import net.fisenko.utils.ids.ulid.random.impl.MonotonicUlidRandomImpl;
import net.fisenko.utils.ids.ulid.random.impl.SimpleUlidRandomImpl;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Fork(value = 2, warmups = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class IdGeneratorsBenchmark {

    @Benchmark
    public void snowflake(ExecutionPlan executionPlan) {
        try {
            executionPlan.snowflakeIdGenerator.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void uuid(ExecutionPlan executionPlan) {
        UUID uuid = UUID.randomUUID();
    }

    @Benchmark
    public void ulidWithMonotonicRandom(ExecutionPlan executionPlan) throws Exception {
        Ulid ulid = executionPlan.monotonicUlidIdGenerator.next();
    }

    @Benchmark
    public void ulidWithSimpleRandom(ExecutionPlan executionPlan) throws Exception {
        Ulid ulid = executionPlan.simpleUlidIdGenerator.next();
    }

    @Benchmark
    public void ulidWithCryptographicallySecureRandom(ExecutionPlan executionPlan) throws Exception {
        Ulid ulid = executionPlan.cryptographicallySecureUlidIdGenerator.next();
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {

        public SnowflakeIdGenerator snowflakeIdGenerator;
        public UlidIdGenerator monotonicUlidIdGenerator;
        public UlidIdGenerator simpleUlidIdGenerator;
        public UlidIdGenerator cryptographicallySecureUlidIdGenerator;

        @Setup(Level.Trial)
        public void setup() {
            this.snowflakeIdGenerator = new SnowflakeIdGeneratorImpl((int) Thread.currentThread().getId());
            this.monotonicUlidIdGenerator = new UlidIdGeneratorImpl(OffsetDateTime.now(ZoneOffset.UTC), new MonotonicUlidRandomImpl());
            this.simpleUlidIdGenerator = new UlidIdGeneratorImpl(OffsetDateTime.now(ZoneOffset.UTC), new SimpleUlidRandomImpl());
            this.cryptographicallySecureUlidIdGenerator = new UlidIdGeneratorImpl(OffsetDateTime.now(ZoneOffset.UTC), new CryptographicallySecureRandomImpl());
        }
    }
}
