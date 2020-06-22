package com.johnchunwai;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
public class JmhLongAdderVsAtomicLong {
    AtomicLong atmLongVal = new AtomicLong();
    LongAdder longAdderVal = new LongAdder();

    @Param({"10", "100", "500"})
    int readToWriteRatio;

    @Benchmark
    public long measureAtomicLong() {
        // just in case readToWriteRatio is a volatile read and the loop below uses too much time.
        int readToWrite = readToWriteRatio;
        for (int i = 0; i < readToWrite; i++) {
            atmLongVal.getAndAdd(1111L);
        }
        // returning the get value avoid deadcode elimination, another approach is to take Blackhole as param and
        // calling blackhole.connsume()
        return atmLongVal.get();
    }

    @Benchmark
    public long measureLongAdder() {
        // just in case readToWriteRatio is a volatile read and the loop below uses too much time.
        int readToWrite = readToWriteRatio;
        for (int i = 0; i < readToWrite; i++) {
            longAdderVal.add(1111L);
        }
        // returning the get value avoid deadcode elimination, another approach is to take Blackhole as param and
        // calling blackhole.connsume()
        return longAdderVal.sum();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JmhLongAdderVsAtomicLong.class.getSimpleName())
                .threads(6)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
