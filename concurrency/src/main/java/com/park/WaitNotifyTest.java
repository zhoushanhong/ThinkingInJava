package com.park;

public class WaitNotifyTest {
    private static final Object lock = new Object();
    private static volatile boolean flag = true;

    /*
     * notify不能指定唤醒那个线程 多半是notifyAll
     * park, unpark能指定唤醒的线程
     */
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    while (flag) {
                        try {
                            System.out.println("wait start ...");
                            lock.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("wait end ....... ");
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    synchronized (lock) {
                        if (flag) {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            lock.notifyAll();
                            System.out.println("notify ... ");
                            flag = false;
                            System.out.println("change flag ... ");
                        }
                    }
                }
            }
        }).start();
    }
}
