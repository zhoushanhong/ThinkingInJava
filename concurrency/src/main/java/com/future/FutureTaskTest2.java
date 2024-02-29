package com.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskTest2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Task task = new Task(); // 自定义的任务
        FutureTask<Integer> futureTask = new FutureTask<Integer>(task); //构建futureTask
        new Thread(futureTask).start(); //作为Runnable入参
        System.out.println("task运行结果: " + futureTask.get());
    }

    static class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("子线程正在计算");
            int sum = 0;

            for (int i = 0; i < 100; i++) {
                sum += i;
            }

            return sum;
        }
    }
}
