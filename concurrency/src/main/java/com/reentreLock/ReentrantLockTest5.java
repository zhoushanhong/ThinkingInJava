package com.reentreLock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest5 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock(true); //公平锁
//        ReentrantLock lock = new ReentrantLock(); //非公平锁

        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                lock.lock();

                try {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " running...");
                } finally {
                    lock.unlock();
                }
            }, "thread-" + i).start();
        }
        
        Thread.sleep(1000); // 1s 之后去争抢锁

        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                lock.lock();

                try {
                    System.out.println(Thread.currentThread().getName() + " running...");
                } finally {
                    lock.unlock();
                }
            }, "插队thread-" + i).start();
        }
    }
}
