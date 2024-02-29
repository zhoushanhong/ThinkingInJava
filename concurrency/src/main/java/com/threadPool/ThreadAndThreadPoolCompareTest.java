package com.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * thread之间有大量的线程切换消耗
 * 所以引入线程池
 * 并不是线程越多越好
 */
public class ThreadAndThreadPoolCompareTest {
    public static void main(String[] args) throws Exception {
        threadPoolTest(); // 快 线程少 上下文切换少
        threadTest(); // 慢 线程多 上下文切换多
    }

    private static void threadTest() throws Exception {
        Long start = System.currentTimeMillis();
        final Random random = new Random();
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    list.add(random.nextInt());

                }
            };
            thread.start();
            thread.join();
        }
        System.out.println("threadTest的时间：" + (System.currentTimeMillis() - start));
        System.out.println("threadTest的list大小：" + list.size());
    }

    private static void threadPoolTest() throws Exception {
        Long start = System.currentTimeMillis();
        final Random random = new Random();
        final List<Integer> list = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    list.add(random.nextInt());
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("threadPoolTest的时间：" + (System.currentTimeMillis() - start));
        System.out.println("threadPoolTest的list大小：" + list.size());
    }
}
