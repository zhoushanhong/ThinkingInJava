package com.synchronize;

public class WrongSyncTest {
    private static volatile int counter = 0;

    public static void increment() {
        counter++;
    }

    public static void decrement() {
        counter--;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                decrement();
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // 理论上 结果应该是0 考虑到并发 且次数相同 应该也是一个比较接近0的值
        // 但是 count会为很大的负值或者正值的原因是
        // 读取同样的初始值 ++ -- 操作回写到主存时有一个覆盖了另一个
        // 长此以往 就出现了很大的负值或者正值
        System.out.println("counter = " + counter);
    }
}
