package com.future;

import java.util.concurrent.*;

public class FutureTaskTest3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> ft1 = new FutureTask<String>(new T1Task());
        FutureTask<String> ft2 = new FutureTask<String>(new T2Task());
        FutureTask<String> ft3 = new FutureTask<String>(new T3Task());
        FutureTask<String> ft4 = new FutureTask<String>(new T4Task());
        FutureTask<String> ft5 = new FutureTask<String>(new T5Task());

        //构建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.submit(ft1);
        executorService.submit(ft2);
        executorService.submit(ft3);
        executorService.submit(ft4);
        executorService.submit(ft5);

        //获取执行结果
        System.out.println(ft1.get());
        System.out.println(ft2.get());
        System.out.println(ft3.get());
        System.out.println(ft4.get());
        System.out.println(ft5.get());

        executorService.shutdown();
    }

    static class T1Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("T1:查询商品基本信息...");
            TimeUnit.MILLISECONDS.sleep(5000);
            return "商品基本信息查询成功";
        }
    }

    static class T2Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("T2:查询商品价格...");
            TimeUnit.MILLISECONDS.sleep(50);
            return "商品价格查询成功";
        }
    }

    static class T3Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("T3:查询商品库存...");
            TimeUnit.MILLISECONDS.sleep(50);
            return "商品库存查询成功";
        }
    }

    static class T4Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("T4:查询商品图片...");
            TimeUnit.MILLISECONDS.sleep(50);
            return "商品图片查询成功";
        }
    }

    static class T5Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("T5:查询商品销售状态...");
            TimeUnit.MILLISECONDS.sleep(50);
            return "商品销售状态查询成功";
        }
    }
}
