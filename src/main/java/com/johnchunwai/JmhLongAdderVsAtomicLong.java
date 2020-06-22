package com.johnchunwai;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
public class JmhLongAdderVsAtomicLong {
    AtomicLong atmLongVal = new AtomicLong();
    LongAdder longAdderVal = new LongAdder();

    @Param({"1", "5", "10"})//, "100", "500"})
    int writeToReadRatio;

    long[] testAddList;

    @Setup(Level.Trial)
    public void setUp() {
        testAddList = new Random().longs().limit(writeToReadRatio).toArray();
    }

    @Benchmark
    public long measureAtomicLong() {
        // just in case readToWriteRatio is a volatile read and the loop below uses too much time.
        int writeToRead = writeToReadRatio;
        for (long v: testAddList) {
            atmLongVal.getAndAdd(v);
        }
        // returning the get value avoid deadcode elimination, another approach is to take Blackhole as param and
        // calling blackhole.connsume()
        return atmLongVal.get();
    }

    @Benchmark
    public long measureLongAdder() {
        // just in case readToWriteRatio is a volatile read and the loop below uses too much time.
        int readToWrite = writeToReadRatio;
        for (long v: testAddList) {
            longAdderVal.add(v);
        }
        // returning the get value avoid deadcode elimination, another approach is to take Blackhole as param and
        // calling blackhole.connsume()
        return longAdderVal.sum();
    }

//    @Benchmark
//    public long measureLongAdderGet() {
//        // just in case readToWriteRatio is a volatile read and the loop below uses too much time.
//        int readToWrite = writeToReadRatio;
//        for (long v: testAddList) {
//            longAdderVal.add(v);
//        }
//        // returning the get value avoid deadcode elimination, another approach is to take Blackhole as param and
//        // calling blackhole.connsume()
//        return longAdderVal.sum();
//    }
//
//    @Benchmark
//    public long measureLongAdder() {
//        // just in case readToWriteRatio is a volatile read and the loop below uses too much time.
//        int readToWrite = writeToReadRatio;
//        for (long v: testAddList) {
//            longAdderVal.add(v);
//        }
//        // returning the get value avoid deadcode elimination, another approach is to take Blackhole as param and
//        // calling blackhole.connsume()
//        return longAdderVal.sum();
//    }

//    public static void main(String[] args) throws RunnerException {
//        Options opt = new OptionsBuilder()
//                .include(JmhLongAdderVsAtomicLong.class.getSimpleName())
//                .threads(6)
//                .forks(1)
//                .build();
//
//        new Runner(opt).run();
//    }
}
