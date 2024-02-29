package com.reentreLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();

            try {
                System.out.println(Thread.currentThread().getName() + " 开始处理任务");
                condition.await(); //会释放当前持有的锁，然后阻塞当前线程
                System.out.println(Thread.currentThread().getName() + " 结束处理任务");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            lock.lock();

            try {
                System.out.println(Thread.currentThread().getName() + " 开始处理任务");
                Thread.sleep(2000);
                condition.signal(); //唤醒因调用Condition#await方法而阻塞的线程
                System.out.println(Thread.currentThread().getName() + " 结束处理任务");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();
    }
}
