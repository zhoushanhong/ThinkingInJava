package com.reentreLock;

import java.util.concurrent.locks.ReentrantLock;


/*
 * lockInterruptibly和lock方法的功能一致 只是等待锁的过程可以被中断
 */
public class ReentrantLockTest3 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {

            System.out.println("t1启动...");

            try {
                lock.lockInterruptibly();
                try {
                    System.out.println("t1获得了锁");
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("t1等锁的过程中被中断");
            }

        }, "t1");

        lock.lock();

        try {
            System.out.println("main线程获得了锁");
            t1.start();
            //先让线程t1执行
            Thread.sleep(1000);

            t1.interrupt();
            System.out.println("线程t1执行中断");
        } finally {
            lock.unlock();
        }

    }
}
