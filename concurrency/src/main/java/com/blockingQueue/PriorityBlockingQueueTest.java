package com.blockingQueue;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueTest {
    public static void main(String[] args) throws InterruptedException {
        //创建优先级阻塞队列  Comparator为null,自然排序
//        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<Integer>(5);

        // 自定义Comparator
        PriorityBlockingQueue queue = new PriorityBlockingQueue<Integer>(5, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });


        Random random = new Random();
        System.out.println("put:");
        for (int i = 0; i < 5; i++) {
            int j = random.nextInt(100);
            System.out.print(j + "  ");
            queue.put(j);
        }

        System.out.println("\ntake:");
        for (int i = 0; i < 5; i++) {
            System.out.print(queue.take() + "  ");
        }


    }
}
