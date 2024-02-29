package com.forkJoinPool;

import com.common.SumUtils;
import com.common.Utils;

import java.time.Duration;
import java.time.Instant;

/**
 * 单线程执行1亿个数求和
 */
public class SumTest1 {
    public static void main(String[] args) {
        // 准备数组
        int[] arr = Utils.buildRandomIntArray(100000000);
        System.out.printf("The array length is: %d\n", arr.length);
        Instant now = Instant.now();

        // 数组求和
        long result = sum(arr);

        System.out.println("执行时间：" + Duration.between(now, Instant.now()).toMillis());
        System.out.printf("The result is: %d\n", result);
    }

    public static long sum(int[] arr){
        return SumUtils.sumRange(arr, 0, arr.length);
    }
}
