package com.park;

public class ThreadStopCorrectWayTest {
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = 0;

                while (!Thread.currentThread().isInterrupted() && count < 1000) {
                    System.out.println("count = " + count++);

                    try {
                        Thread.sleep(100); // 会被外部的interrupt方法中断
                    } catch (Exception e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }

                System.out.println("stop thread");
            }
        };

        Thread t = new Thread(runnable);
        t.start();

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        t.interrupt();
    }
}
