package com.reentreLock;

public class ReentrantLockTest1 {
    private static int sum = 0;
//    private static Lock lock = new ReentrantLock();
    private static MyLock lock = new MyLock();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(()->{
                lock.lock(); //加锁

                try {
                    // 临界区代码
                    for (int j = 0; j < 10000; j++) {
                        sum++;
                    }
                } finally {
                    lock.unlock(); // 解锁
                }
            });

            thread.start();
        }

        Thread.sleep(2000);
        System.out.println(sum);
    }
}
