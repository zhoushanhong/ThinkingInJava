package com.forkJoinPool;

import com.common.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * 多线程求1亿个数的和 ForkJoin
 */
public class SumTest4 {
    private static final int NCPU = Runtime.getRuntime().availableProcessors(); // 获取逻辑处理器数量
    private static long sum; // 计算结果

    public static void main(String[] args) throws Exception {
        int[] array = Utils.buildRandomIntArray(100000000); //准备数组

        // 单线程
        Instant now = Instant.now();
        sum = new SeqSum().calc(array); // 单线程计算数组总和
        System.out.println("seq sum = " + sum);
        System.out.println("执行时间："+ Duration.between(now,Instant.now()).toMillis());

        // ForkJoin
        MultiSum ls = new MultiSum(array, 0, array.length); //递归任务
        ForkJoinPool fjp  = new ForkJoinPool(NCPU); // 构建ForkJoinPool

        now = Instant.now();
        ForkJoinTask<Long> result = fjp.submit(ls); //ForkJoin计算数组总和
        System.out.println("ForkJoin sum = " + result.get());
        System.out.println("执行时间："+ Duration.between(now,Instant.now()).toMillis());
        fjp.shutdown();

        // 并行流计算数组总和
        now = Instant.now();
        Long sum = (Long) IntStream.of(array).asLongStream().parallel().sum();
        System.out.println("IntStream sum = " + sum);
        System.out.println("执行时间：" + Duration.between(now,Instant.now()).toMillis());
    }

    // 单线程算法
    private static class SeqSum {
        private static long calc(int[] array) {
            long sum = 0;

            for (int i = 0; i < array.length; ++i) {
                sum += array[i];
            }

            return sum;
        }
    }

    // 多线程算法
    private static class MultiSum extends RecursiveTask<Long> {
        static final int SEQUENTIAL_THRESHOLD = 10000000; // 任务拆分最小阈值

        private int low;
        private int high;
        private int[] array;

        public MultiSum(int[] arr, int lo, int hi) {
            array = arr;
            low = lo;
            high = hi;
        }

        @Override
        protected Long compute() {
            //当任务拆分到小于等于阀值时开始求和
            if (high - low <= SEQUENTIAL_THRESHOLD) {
                long sum = 0;
                for (int i = low; i < high; ++i) {
                    sum += array[i];
                }

                return sum;
            } else {  // 任务过大继续拆分
                int mid = low + (high - low) / 2;
                MultiSum left = new MultiSum(array, low, mid);
                MultiSum right = new MultiSum(array, mid, high);

                // 提交任务
                left.fork();
                right.fork();

                //获取任务的执行结果,将阻塞当前线程直到对应的子任务完成运行并返回结果
                long rightAns = right.compute();
                long leftAns = left.join();
                return leftAns + rightAns;
            }
        }
    }
}
