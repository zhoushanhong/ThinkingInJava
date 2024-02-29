package com.cas;

import com.common.CASLock;

public class CASDemo {
    private static volatile int sum = 0;
    private static CASLock casLock = new CASLock();

    public static void main(String[] args) {
        casTest();
        System.out.println(sum);
    }

    private static void casTest() {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (;;) {
                    if (casLock.getState() == 0 && casLock.cas()) {
                        try {
                            for (int j = 0; j < 10000; j++) {
                                sum++;
                            }
                        } finally {
                            casLock.setState(0);
                        }
                    }

                    break;
                }
            });

            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
