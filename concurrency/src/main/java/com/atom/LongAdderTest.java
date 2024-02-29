package com.atom;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/*
 * 一般的业务场景下AtomicLong是足够了。
 * 如果并发量很多，存在大量写多读少的情况，那LongAdder可能更合适
 * 线程数越多，并发操作数越大，LongAdder的优势越明显
 * 影响性能的本质问题是AtomicLong的自旋问题
 * LongAdder解决自旋等待冲突的办法是 散列热点值 减少冲突
 */
public class LongAdderTest {
    public static void main(String[] args) throws Exception {
        atomicLongCompareLongAdderTest(10, 10000);
        System.out.println("==================");
        atomicLongCompareLongAdderTest(10, 200000);
        System.out.println("==================");
        atomicLongCompareLongAdderTest(100, 200000);
    }

    private static void atomicLongCompareLongAdderTest(final int threadCount, final int times) {
        try {
            long start = System.currentTimeMillis();
            longAdderTest(threadCount, times);
            long end = System.currentTimeMillis() - start;
            System.out.println("条件>>>>>>线程数: " + threadCount + ", 单线程操作计数" + times);
            System.out.println("结果>>>>>>LongAdder方式增加计数" + (threadCount * times) + "次,共计耗时:" + end);

            long start2 = System.currentTimeMillis();
            atomicLongTest(threadCount, times);
            long end2 = System.currentTimeMillis() - start2;
            System.out.println("条件>>>>>>线程数: " + threadCount + ", 单线程操作计数" + times);
            System.out.println("结果>>>>>>AtomicLong方式增加计数" + (threadCount * times) + "次,共计耗时:" + end2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void atomicLongTest(final int threadCount, final int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicLong atomicLong = new AtomicLong();
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < times; j++) {
                        atomicLong.incrementAndGet();
                    }
                    countDownLatch.countDown();
                }
            }, "atomicLong-thread" + i).start();
        }
        countDownLatch.await();
    }

    private static void longAdderTest(final int threadCount, final int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < times; j++) {
                        longAdder.add(1);
                    }
                    countDownLatch.countDown();
                }
            }, "longAdder-thread" + i).start();
        }

        countDownLatch.await();
    }
}
