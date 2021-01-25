# Benchmarks

## Configuration

### Hardware

```txt
Processor Name: 8-Core Intel Core i9
Processor Speed: 2.4 GHz
Number of Processors: 1
Total Number of Cores: 8
L2 Cache (per Core): 256 KB
L3 Cache: 16 MB
Hyper-Threading Technology: Enabled
Memory: 32 GB
```

### Test Configuration 

```txt
# JMH version: 1.23
# VM version: JDK 11.0.9, OpenJDK 64-Bit Server VM, 11.0.9+11-LTS
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
```

## Results

```txt
Benchmark                                                     Mode  Cnt      Score      Error   Units
IdGeneratorsBenchmark.snowflake                              thrpt    6    493.191 ±   10.849  ops/ms
IdGeneratorsBenchmark.ulidWithCryptographicallySecureRandom  thrpt    6   4434.170 ±  192.941  ops/ms
IdGeneratorsBenchmark.ulidWithMonotonicRandom                thrpt    6  21024.702 ±  376.238  ops/ms
IdGeneratorsBenchmark.ulidWithSimpleRandom                   thrpt    6  22459.598 ± 1462.661  ops/ms
IdGeneratorsBenchmark.uuid                                   thrpt    6   3280.281 ±  116.517  ops/ms
```
