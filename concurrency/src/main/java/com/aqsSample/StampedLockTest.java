package com.aqsSample;

import java.util.concurrent.locks.StampedLock;

public class StampedLockTest {
    public static void main(String[] args) throws InterruptedException {
        Point point = new Point();

        //第一次移动x,y
        new Thread(()-> point.move(100,200)).start();
        Thread.sleep(100);

        new Thread(()-> point.distanceFromOrigin()).start();
        Thread.sleep(500);

        //第二次移动x,y
        new Thread(()-> point.move(300,400)).start();
    }

    private static class Point {
        private final StampedLock stampedLock = new StampedLock();

        private volatile double x;
        private volatile double y;

        public void move(double deltaX, double deltaY) {
            // 获取写锁
            long stamp = stampedLock.writeLock();
            System.out.println("获取到writeLock");
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                // 释放写锁
                stampedLock.unlockWrite(stamp);
                System.out.println("释放writeLock");
            }
        }

        // 计算当前坐标到原点的距离
        public double distanceFromOrigin() {
            // 获得一个乐观读锁
            long stamp = stampedLock.tryOptimisticRead(); // 有乐观锁的概念 并不是一定要阻塞

            double currentX = x;
            System.out.println("第1次读, x:" + x + ", y:" + y + ", currentX:" + currentX);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double currentY = y;
            System.out.println("第2次读, x:" + x + ", y:" + y + ", currentX:" + currentX + ", currentY:" + currentY);

            // 检查乐观读锁后是否有其他写锁发生
            if (!stampedLock.validate(stamp)) {
                // 获取一个悲观读锁
                stamp = stampedLock.readLock();
                try {
                    currentX = x;
                    currentY = y;

                    System.out.println("最终结果, x:" + x + ", y:" + y + ", currentX:" + currentX + ", currentY:" + currentY);
                } finally {
                    // 释放悲观读锁
                    stampedLock.unlockRead(stamp);
                }
            }

            double distance = Math.sqrt(currentX * currentX + currentY * currentY);
            System.out.println("距离为:" + distance);

            return distance;
        }
    }
}

