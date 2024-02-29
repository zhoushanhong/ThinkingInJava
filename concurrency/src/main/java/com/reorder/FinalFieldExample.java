package com.reorder;

public class FinalFieldExample {
    private final int x;
    private int y;
    private static FinalFieldExample f;

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    f = new FinalFieldExample();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int i = 0, j = 0;

                    if (null != f) {
                        i = f.x; // x保证一定是3 final保证的可见性
                        j = f.y; // y不一定是4 可能是0
                    }

                    System.out.println("i = " + i + " , j = " + j);
                    if (j == 0) { // 很难复现出来
                        break;
                    }
                }
            }
        }).start();
    }

    public FinalFieldExample() {
        x = 3;
        y = 4;
    }
}
