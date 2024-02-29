package com.forkJoinPool;

import com.common.SumUtils;
import com.common.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

/**
 * 多线程求1亿个数的和 非递归方式
 */
public class SumTest2 {
    public final static int NUM = 10000000; // 拆分的粒度

    public static void main(String[] args) throws Exception {
        int[] arr = Utils.buildRandomIntArray(100000000); // 准备数组
        int numThreads = arr.length / NUM > 0 ? arr.length / NUM : 1; //获取线程数
        System.out.printf("The array length is: %d\n", arr.length);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads); // 构建线程池
        ((ThreadPoolExecutor)executor).prestartAllCoreThreads(); // 预热线程池

        Instant now = Instant.now();
        long result = sum(arr, executor); // 数组求和
        System.out.println("执行时间：" + Duration.between(now,Instant.now()).toMillis());
        System.out.printf("The result is: %d\n", result);

        executor.shutdown();
    }

    public static long sum(int[] arr, ExecutorService executor) throws Exception {
        long result = 0;
        int numThreads = arr.length / NUM > 0 ? arr.length / NUM : 1;

        //任务分解
        SumTask[] tasks = new SumTask[numThreads]; // 任务集
        Future<Long>[] sums = new Future[numThreads]; // 结果集
        for (int i = 0; i < numThreads; i++) {
            tasks[i] = new SumTask(arr, (i * NUM), ((i + 1) * NUM));
            sums[i] = executor.submit(tasks[i]);
        }

        //结果合并
        for (int i = 0; i < numThreads; i++) {
            result += sums[i].get();
        }

        return result;
    }

    private static class SumTask implements Callable<Long> {
        private int lo;
        private int hi;
        private int[] arr;

        public SumTask(int[] a, int l, int h) {
            lo = l;
            hi = h;
            arr = a;
        }

        @Override
        public Long call() {
            //System.out.printf("The range is [%d - %d]\n", lo, hi);
            long result = SumUtils.sumRange(arr, lo, hi);
            return result;
        }
    }
}
