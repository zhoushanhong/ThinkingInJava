package com.blockingQueue;

import java.util.concurrent.TransferQueue;

public class LinkedTransferQueueConsumer implements Runnable {
    private final TransferQueue<String> queue;

    public LinkedTransferQueueConsumer(TransferQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            System.out.println("Consumer " + Thread.currentThread().getName() + queue.take());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
