package com.reorder;

public class ReOrderTest {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws Exception {
        int i = 0;
        while (true) {
            i++;

            x = 0; y = 0; a = 0; b = 0;

            // 如果没有指令重排序 那么xy的排列集合中 00是不会出现的
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    a = 1;
                    x = b;
                }
            });


            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    b = 1;
                    y = a;
                }
            });

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            System.out.println("第" + i + "次（" + x + "," + y + ")");
            if (x == 0 && y == 0) {
                break;
            }
        }

    }

    private static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;

        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }



}
