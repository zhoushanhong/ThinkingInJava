package com.park;

public class ThreadJoinTest {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread t begin ... ");

                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("Thread t end");
            }
        });

        long start = System.currentTimeMillis();
        t.start();
        t.join();

        System.out.println("执行时间:" + (System.currentTimeMillis() - start));
        System.out.println("Main end");
    }
}
