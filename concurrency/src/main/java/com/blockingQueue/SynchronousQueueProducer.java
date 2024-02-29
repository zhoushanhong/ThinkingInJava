package com.blockingQueue;

import java.util.concurrent.BlockingQueue;

public class SynchronousQueueProducer implements Runnable {
    protected BlockingQueue<Integer> blockingQueue;

    public SynchronousQueueProducer(BlockingQueue<Integer> queue) {
        this.blockingQueue = queue;
    }

    @Override
    public void run() {
        int i = 0;
        while (i < 10) {
            System.out.println(Thread.currentThread().getName() + " Put: " + ++i);
            try {
                blockingQueue.put(i);
                Thread.sleep(1000);  //每隔一秒生产一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
