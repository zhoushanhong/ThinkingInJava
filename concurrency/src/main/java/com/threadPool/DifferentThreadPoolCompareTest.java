package com.threadPool;

import java.util.concurrent.*;

public class DifferentThreadPoolCompareTest {
    public static void main(String[] args) {
        // TODO: 怎么统计doTask消耗的时间

//        cacheThreadPoolTest(); // 快 每个任务分配一个新的线程
//        fixedThreadPoolTest(); // 中 10个核心线程 最大线程为10
//        singleThreadPoolTest(); // 慢 单个核心线程 最大线程为1
        customizeThreadPoolTest();
    }

    private static void cacheThreadPoolTest() {
        ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
        doTask(cacheThreadPool);
    }

    private static void fixedThreadPoolTest() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        doTask(fixedThreadPool);
    }

    private static void singleThreadPoolTest() {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        doTask(singleThreadPool);
    }

    private static void customizeThreadPoolTest() {
        ThreadPoolExecutor customizeThreadPool = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS,
                                                                        new LinkedBlockingQueue<>(10),
                                                                        new MyRejectedExecutionHandler());
        doTask(customizeThreadPool);
    }

    private static void doTask(ExecutorService executorService) {
        for (int i = 1; i < 100; i++) { // i = 1 直观的比对threadId和taskId的关系
            executorService.execute(new MyTask(i));
        }
    }
}
