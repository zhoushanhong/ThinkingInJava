package com.visibility;

public class VolatileNotAtomTest {
    private volatile static int sum = 0;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        sum++;
                    }
                }
            });

            thread.start();
//            thread.join();
        }

        Thread.sleep(3000);

        System.out.println(sum);
    }
}
