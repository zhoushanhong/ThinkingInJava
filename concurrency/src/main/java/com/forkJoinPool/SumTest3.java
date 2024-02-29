package com.forkJoinPool;

import com.common.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 多线程求1亿个数的和 递归方式
 */
public class SumTest3 {
    public static void main(String[] args) throws Exception {
        //准备数组
        int[] arr = Utils.buildRandomIntArray(100000000);
        System.out.printf("The array length is: %d\n", arr.length);
        Instant now = Instant.now();

        //数组求和
        long result = sum(arr);
        System.out.println("执行时间："+ Duration.between(now,Instant.now()).toMillis());
        System.out.printf("The result is: %d\n", result);
    }

    public static long sum(int[] arr) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
//        ExecutorService executorService = Executors.newFixedThreadPool(12); // 定长线程的饥饿

        RecursiveSumTask task = new RecursiveSumTask(executorService, arr, 0, arr.length); //递归任务 求和
        long result = executorService.submit(task).get(); //返回结果
        executorService.shutdown();

        return result;
    }

    public static class RecursiveSumTask implements Callable<Long> {
        //拆分的粒度
        private static final int SEQUENTIAL_CUTOFF = 100000; // 最小拆分的阈值
        private int lo;
        private int hi;
        private int[] array; // arguments
        private ExecutorService executorService;

        RecursiveSumTask(ExecutorService executorService, int[] array, int l, int h) {
            this.executorService = executorService;
            this.array = array;
            this.lo = l;
            this.hi = h;
        }

        @Override
        public Long call() throws Exception {
            System.out.format("%s range [%d-%d] begin to compute %n", Thread.currentThread().getName(), lo, hi);
            long result = 0;

            if (hi - lo <= SEQUENTIAL_CUTOFF) {
                for (int i = lo; i < hi; i++) {
                    result += array[i];
                }
                System.out.format("%s range [%d-%d] begin to finished %n", Thread.currentThread().getName(), lo, hi);
            } else {
                RecursiveSumTask left = new RecursiveSumTask(executorService, array, lo, (hi + lo) / 2);
                RecursiveSumTask right = new RecursiveSumTask(executorService, array, (hi + lo) / 2, hi);
                Future<Long> lr = executorService.submit(left);
                Future<Long> rr = executorService.submit(right);

                result = lr.get() + rr.get();
                System.out.format("%s range [%d-%d] finished to compute %n", Thread.currentThread().getName(), lo, hi);
            }

            return result;
        }
    }
}
