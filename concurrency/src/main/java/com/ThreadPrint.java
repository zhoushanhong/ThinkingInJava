package com;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


// 三个线程交替打印 0 - 100
public class ThreadPrint {
    private static volatile int index = 0;
    private final static ReentrantLock lock = new ReentrantLock();
    private final static Condition remainder0 = lock.newCondition();
    private final static Condition remainder1 = lock.newCondition();
    private final static Condition remainder2 = lock.newCondition();

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(new Task1());
        Thread t2 = new Thread(new Task2());
        Thread t3 = new Thread(new Task3());

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }

    private static class Task1 implements Runnable {
        @Override
        public void run() {
            while (index <= 100) {
//                System.out.println("task1 执行");
                try {
                    lock.lock();
//                    System.out.println("task1 加锁");

                    if (index % 3 != 0) {
//                        System.out.println("task1 进入条件等待队列");
                        remainder0.await();
                    }

                    if (index > 100) {
                        break;
                    }

                    System.out.println(index);
                    index++;
//                    System.out.println("唤醒task2");
                    remainder1.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private static class Task2 implements Runnable {
        @Override
        public void run() {
            while (index <= 100) {
//                System.out.println("task2 执行");
                try {
                    lock.lock();
//                    System.out.println("task2 加锁");

                    if (index % 3 != 1) {
//                        System.out.println("task2 进入条件等待队列");
                        remainder1.await();
                    }

                    if (index > 100) {
                        break;
                    }

                    System.out.println(index);
                    index++;
//                    System.out.println("唤醒task3");
                    remainder2.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private static class Task3 implements Runnable {
        @Override
        public void run() {
            while (index <= 100) {
//                System.out.println("task3 执行");
                try {
                    lock.lock();
//                    System.out.println("task3 加锁");

                    if (index % 3 != 2) {
//                        System.out.println("task3 进入条件等待队列");
                        remainder2.await();
                    }

                    if (index > 100) {
                        break;
                    }

                    System.out.println(index);
                    index++;
//                    System.out.println("唤醒task1");
                    remainder0.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
