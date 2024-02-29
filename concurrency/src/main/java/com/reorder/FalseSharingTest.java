package com.reorder;

/*
 * 伪共享
 */
public class FalseSharingTest {
    public static void main(String[] args) throws Exception {
        Point point = new Point();

        long start = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                point.x++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                point.y++;
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // 线程安全 但是volatile的修饰拉低了性能表现
        System.out.println(point.x + "," + point.y);
        System.out.println(System.currentTimeMillis() - start);
    }

    // 去掉volatile后性能提升
    static class Point {
        // -XX:-RestrictContended
        // @Contended
        private volatile long x;
        private long p1, p2, p3, p4, p5, p6, p7; // 字节填充后 性能提升
        private volatile long y;
    }
}
