package com.park;

import java.util.concurrent.locks.LockSupport;

public class UnParkBeforeParkTest {
    public static void main(String[] args) {
        Thread parkThread = new Thread(new ParkThread());
        parkThread.start();

        // 只要线程启动 unpark在park之前也是有效的
        System.out.println("唤醒parkThread");
        LockSupport.unpark(parkThread);
    }

    private static class ParkThread implements Runnable {

        @Override
        public void run() {
            System.out.println("ParkThread开始执行");
            LockSupport.park();
            System.out.println("ParkThread执行完成");
        }
    }
}
