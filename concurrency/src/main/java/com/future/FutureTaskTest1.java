package com.future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTaskTest1 {
    public static void main(String[] args) throws Exception {

        new Thread(() -> {
            System.out.println("通过Runnable方式执行任务");
        }).start();

        FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println("通过Callable方式执行任务");
                Thread.sleep(3000);
                return "返回任务结果";
            }

        });

        new Thread(task).start();
        System.out.println("结果: " + task.get());
    }
}
