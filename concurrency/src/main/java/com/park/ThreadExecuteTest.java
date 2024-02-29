package com.park;

import java.util.concurrent.*;

public class ThreadExecuteTest {
    public static void main(String[] args) {
//        runnableTest();
//        futureTaskTest();
        futureTest();
    }

    private static void runnableTest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("通过Runnable方式执行任务");
            }
        };

        new Thread(runnable).start(); // 通过启动线程回调执行run方法 有线程资源
//        new Thread(runnable).run(); // 只是调用对象的方法 没有线程资源
    }

    private static void futureTaskTest() {
        FutureTask<String> task = new FutureTask<String>(new Callable() {
            @Override
            public String call() throws Exception {
                System.out.println("通过Runnable方式执行任务");
                Thread.sleep(3000);
                return "返回任务结果";
            }
        });

        new Thread(task).start();

        try {
            System.out.println(task.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void futureTest() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<String> future1 = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("开始煮饭");

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "饭熟了";
            }
        });

        Future<String> future2 = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("开始做菜");

                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return "菜好了";
            }
        });

        try {
            System.out.println(future1.get() + " , " + future2.get() + " , 开始吃饭...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }

}
