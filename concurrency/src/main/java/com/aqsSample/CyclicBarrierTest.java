package com.aqsSample;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "开始等待其他线程");

                        cyclicBarrier.await(); // 先等待 等待的线程等于3了之后 通过执行
                        System.out.println(Thread.currentThread().getName() + "开始执行");
                        Thread.sleep(5000);  // 模拟业务处理

                        System.out.println(Thread.currentThread().getName() + "执行完毕");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
