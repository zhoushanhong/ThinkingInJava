package com.park;

import java.util.concurrent.locks.LockSupport;

public class ThreadStateTest {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            LockSupport.park();
        });

        System.out.println("线程状态: " + thread.getState());
        thread.start();
        System.out.println("线程状态: " + thread.getState());
        thread.sleep(1000);
        System.out.println("线程状态: " + thread.getState());
    }
}
