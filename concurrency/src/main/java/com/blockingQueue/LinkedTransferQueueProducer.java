package com.blockingQueue;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

public class LinkedTransferQueueProducer implements Runnable {
    private final TransferQueue<String> queue;

    public LinkedTransferQueueProducer(TransferQueue<String> queue) {
        this.queue = queue;
    }

    private String produce() {
        return " Your lucky number " + (new Random().nextInt(100));
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (queue.hasWaitingConsumer()) {
                    queue.transfer(produce());
                }
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e){

        }
    }
}
