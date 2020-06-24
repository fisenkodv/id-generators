package net.fisenko.utils.ids.benchmark;

import net.fisenko.utils.ids.snowflake.IdGenerator;
import net.fisenko.utils.ids.snowflake.InvalidSystemClockException;
import net.fisenko.utils.ids.snowflake.SequenceOverflowException;
import net.fisenko.utils.ids.snowflake.SnowflakeIdGenerator;
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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Fork(value = 2, warmups = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class IdGeneratorsBenchmark {
    @Benchmark
    public void snowflake(ExecutionPlan executionPlan) {
        try {
            executionPlan.generator.createId();
        } catch (InvalidSystemClockException | SequenceOverflowException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void uuid(ExecutionPlan executionPlan) {
        UUID uuid = UUID.randomUUID();
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public IdGenerator<Long> generator;

        @Setup(Level.Trial)
        public void setup() {
            this.generator = new SnowflakeIdGenerator((int) Thread.currentThread().getId());
        }
    }
}
