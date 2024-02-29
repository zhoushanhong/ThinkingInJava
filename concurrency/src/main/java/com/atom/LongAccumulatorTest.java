package com.atom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

public class LongAccumulatorTest {
    public static void main(String[] args) throws InterruptedException {
        // 累加 y * y
        // x是上一次运算的结果 y是这一次运算的参数
        LongAccumulator accumulator = new LongAccumulator((x, y) -> x + y * y, 0);

        // 开启8个线程完成 1^2 + 2^2 + 3^2 + 4^2 ... + 9^2 的累加 sum的值是一个更新的热点
        // 和LongAdder的差别在于 LongAdder只能简单的做加减运算 但是LongAccumulator可以是一个任意的算术表达式
        ExecutorService executor = Executors.newFixedThreadPool(8);
        IntStream.range(1, 10).forEach(i -> executor.submit(() -> accumulator.accumulate(i)));

        Thread.sleep(2000);
        System.out.println(accumulator.getThenReset());

    }
}
