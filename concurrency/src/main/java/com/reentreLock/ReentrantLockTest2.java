package com.reentreLock;

import java.util.concurrent.locks.ReentrantLock;

/*
 * 可重入
 */
public class ReentrantLockTest2 {
    public static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        method1();
    }
    
    public static void method1() {
        lock.lock();
        try {
            System.out.println("execute method1");
            method2();
        } finally {
            lock.unlock();
        }
    }
    public static void method2() {
        lock.lock();
        try {
            System.out.println("execute method2");
            method3();
        } finally {
            lock.unlock();
        }
    }
    public static void method3() {
        lock.lock();
        try {
            System.out.println("execute method3");
        } finally {
            lock.unlock();
        }
    }
}
