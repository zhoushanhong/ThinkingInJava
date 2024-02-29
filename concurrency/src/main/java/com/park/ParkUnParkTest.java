package com.park;

import java.util.concurrent.locks.LockSupport;

public class ParkUnParkTest {
    private volatile Object store = null;
    private Object goodsLock = new Object();

    public static void main(String[] args) {
//        new ParkUnParkTest().parkUnparkTest();
//        new ParkUnParkTest().parkUnparkExceptionTest();
//        new ParkUnParkTest().moreUnparkTest();
        new ParkUnParkTest().moreParkTest();
    }

    /*
     * 正常使用方式
     */
    private void parkUnparkTest() {
        Thread consumer = new Thread(() -> {
            System.out.println("consumer wait store open ... ");

            while (null == store) {
                LockSupport.park();
                System.out.println("buy goods success!");
            }
        });
        consumer.start();

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        store = new Object();
        LockSupport.unpark(consumer);

        System.out.println("store open, notify consumer ... ");
    }


    /*
     * 死锁
     */
    private void parkUnparkExceptionTest() {
        Thread consumer = new Thread(() -> {
            System.out.println("consumer wait store open ... ");

            if (null == store) {
                synchronized (goodsLock) {
                    LockSupport.park();
                    System.out.println("buy goods success!");
                }
            }
        });
        consumer.start();

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        store = new Object();

        synchronized (goodsLock) {
            LockSupport.unpark(consumer);
            System.out.println("store open, notify consumer ... ");
        }
    }

    /*
     * 单次park 多次unpark 线程正常运行
     */
    public void moreUnparkTest() {
        LockSupport.unpark(Thread.currentThread());
        LockSupport.unpark(Thread.currentThread());
        LockSupport.unpark(Thread.currentThread());
        System.out.println("调用了三次unpark");

        LockSupport.park(Thread.currentThread());
        System.out.println("调用了一次park");
    }

    /*
     * 多次park 单次unpark 等待, 即使调用相同次数的unpark也不会正常唤醒
     */
    public void moreParkTest() {
        LockSupport.park(Thread.currentThread());
        System.out.println("调用了一次park");

        LockSupport.park(Thread.currentThread());
        LockSupport.park(Thread.currentThread());
        System.out.println("又调用了两次park");

        LockSupport.unpark(Thread.currentThread());
        System.out.println("调用了一次unpark方法");

//        LockSupport.unpark(Thread.currentThread());
//        LockSupport.unpark(Thread.currentThread());
//        System.out.println("又调用了两次unpark方法");
    }
}
