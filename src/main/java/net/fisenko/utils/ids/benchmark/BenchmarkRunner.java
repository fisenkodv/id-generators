package net.fisenko.utils.ids.benchmark;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(IdGeneratorsBenchmark.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }
}
