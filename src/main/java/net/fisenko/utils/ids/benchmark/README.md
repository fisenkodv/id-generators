# Benchmarks

## Configuration
```txt
# JMH version: 1.23
# VM version: JDK 11.0.7, OpenJDK 64-Bit Server VM, 11.0.7+10-b765.53
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
```

## Results
| Benchmark | Mode  | Cnt | Score    | Error    | Units  |
| :-------- | ----- | --- | -------- | -------- | ------ |
| snowflake | thrpt | 6   | 374.869  | ± 86.832 | ops/ms |
| uuid      | thrpt | 6   | 2616.008 | ± 92.965 | ops/ms |
