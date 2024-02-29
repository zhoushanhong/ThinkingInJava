package com.aqsSample;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/*
 * 让单个线程等待：多个线程(任务)完成后，进行汇总合并
 */
public class CountDownLatchTest2 {
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(1000));
                    System.out.println(Thread.currentThread().getName() + " finish task" + index);
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // 主线程在阻塞，当计数器==0，就唤醒主线程往下执行。
        countDownLatch.await();
        System.out.println("主线程:在所有任务运行完成后，进行结果汇总");

    }
}
