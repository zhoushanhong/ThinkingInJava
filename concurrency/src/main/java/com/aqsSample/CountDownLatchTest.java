package com.aqsSample;

import java.util.concurrent.CountDownLatch;

/*
 * 让多个线程等待：模拟并发，让并发线程一起执行
 */
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    //准备完毕…… 运动员都阻塞在等待号令
                    countDownLatch.await();
                    String parter = "[" + Thread.currentThread().getName() + "]";
                    System.out.println(parter + "prepare to run ... ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Thread.sleep(2000);// 裁判准备发令
        countDownLatch.countDown();// 发令枪：执行发令
    }
}
