package com.blockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayBlockingQueueTest {
    public static void main(String[] args) throws Exception {
        // 使用ArrayBlockingQueue初始化一个BlockingQueue，指定容量的上限为1024
        BlockingQueue queue = new ArrayBlockingQueue(1024);

        Producer producer = new Producer(queue);  // 生产者
        Consumer consumer = new Consumer(queue);  // 消费者

        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(consumer);

        t1.start(); // 开启生产者线程
        t2.start(); // 开启消费者线程

        t1.join();
        t2.join();
    }
}
