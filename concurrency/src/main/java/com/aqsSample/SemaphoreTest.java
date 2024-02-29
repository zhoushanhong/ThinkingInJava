package com.aqsSample;

import java.util.concurrent.Semaphore;

/*
 * Semaphore是一个计数信号量, Semaphore经常用于限制获取资源的线程数量
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        // 声明3个窗口
        Semaphore windows = new Semaphore(3);

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        windows.acquire(); // 占用窗口 加锁

                        System.out.println(Thread.currentThread().getName() + ": 开始买票");
                        Thread.sleep(5000); //模拟买票流程
                        System.out.println(Thread.currentThread().getName() + ": 购票成功");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        windows.release(); // 释放窗口
                    }
                }
            }).start();
        }
    }
}
