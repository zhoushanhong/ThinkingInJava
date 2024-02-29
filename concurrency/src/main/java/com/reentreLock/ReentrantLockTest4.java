package com.reentreLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest4 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            System.out.println("t1启动...");

            // 注意： 即使是设置的公平锁，此方法也会立即返回获取锁成功或失败，公平策略不生效
//            if (!lock.tryLock()) {
//                System.out.println("t1获取锁失败，立即返回false");
//                return;
//            }

            //超时
            try {
                if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                    System.out.println("等待 1s 后获取锁失败，返回");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            try {
                System.out.println("t1获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();

        try {
            System.out.println("main线程获得了锁");
            t1.start(); //先让线程t1执行
            Thread.sleep(2000);
        } finally {
            lock.unlock();
        }
    }
}
