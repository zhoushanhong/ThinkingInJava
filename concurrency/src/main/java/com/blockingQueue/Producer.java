package com.blockingQueue;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    protected BlockingQueue queue = null;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for(int i = 0; i < 3; i++) {
                queue.put(i);
                System.out.println("produce " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
