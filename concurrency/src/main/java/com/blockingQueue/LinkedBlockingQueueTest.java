package com.blockingQueue;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueTest {
    private static LinkedBlockingQueue<String> linkedBlockingQueue;

    public static void main(String[] args) {
        linkedBlockingQueue = new LinkedBlockingQueue<String>(20);
        addTest();
//        offerTest();
//        pollTest();
//        peekTest();
//        takeTest(); // 阻塞
//        putTest(); // 阻塞
    }

    private static void addTest () {
        // 1、add 方法: 队列已满，报java.lang.IllegalStateException: Queue full 错误
        System.out.println("-----add方法-----");
        for (int i = 0; i < 25; i++) {
            linkedBlockingQueue.add(String.valueOf(i));
        }
        System.out.println(linkedBlockingQueue.size());
    }

    private static void offerTest() {
        // 2、offer 方法，队列已满，程序正常运行，只是不再新增元素
        System.out.println("-----offer方法-----");
        for (int i = 0; i < 25; i++) {
            linkedBlockingQueue.offer(String.valueOf(i));
        }
        System.out.println(linkedBlockingQueue.size());
    }

    private static void pollTest() {
        offerTest();

        System.out.println("-----poll方法-----");
        // 3、poll 方法，弹出队顶元素，队列为空时返回null
        for (int i = 0; i < 6; i++) {
            String e = linkedBlockingQueue.poll();
            System.out.println("取出元素：" + e);
        }
    }

    private static void peekTest() {
        offerTest();

        System.out.println("-----peek方法-----");
        // 4、peek 方法，返回队列顶元素，但顶元素不弹出，队列为空时返回null
        for (int i = 0; i < 5; i++) {
            String e = linkedBlockingQueue.peek();
            System.out.println("取出元素：" + e);
        }
    }

    private static void takeTest() {
        offerTest();

        System.out.println("-----take方法-----");
        // 5、take 方法，当队列为空，阻塞
        for (int i = 0; i < 25; i++) {
            String e = null;
            try {
                e = linkedBlockingQueue.take();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println("取出元素：" + e);
        }
    }

    private static void putTest() {
        System.out.println("-----put方法-----");
        // 6、put 方法，当队列满时，阻塞
        for (int i = 0; i < 30; i++) {
            try {
                String e = String.valueOf(i);
                linkedBlockingQueue.put(e);
                System.out.println("放入元素：" + e);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("队列深度：" + linkedBlockingQueue.size());
    }
}
