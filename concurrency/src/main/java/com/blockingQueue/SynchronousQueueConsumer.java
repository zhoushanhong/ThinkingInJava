package com.blockingQueue;

import java.util.concurrent.BlockingQueue;

public class SynchronousQueueConsumer implements Runnable {
    protected BlockingQueue<Integer> blockingQueue;

    public SynchronousQueueConsumer(BlockingQueue<Integer> queue) {
        this.blockingQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Integer data = blockingQueue.take();
                System.out.println(Thread.currentThread().getName() + " take(): " + data);
                Thread.sleep(5000);  //每隔5秒消费一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
