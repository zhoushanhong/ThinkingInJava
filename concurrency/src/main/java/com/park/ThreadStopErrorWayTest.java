package com.park;

public class ThreadStopErrorWayTest {
    private static Object lock = new Object();

    /*
     * 等待2秒后 获取锁 之前的线程没有执行完 类似于强制关机的概念
     */
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + " get lock ... ");
                    try {
                        Thread.sleep(60000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(Thread.currentThread().getName() + " end");
            }
        });

        thread.start();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        thread.stop(); // 不会执行end的输出 会释放锁
        thread.suspend(); // 不会执行end的输出 不会释放锁

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " waiting lock ... ");
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + " get lock");
                }
            }
        }).start();
    }
}
