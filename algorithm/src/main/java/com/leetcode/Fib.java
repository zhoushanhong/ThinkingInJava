package com.leetcode;

import java.util.ArrayList;
import java.util.List;

// 509 斐波那契数
public class Fib {
    private static final List<Integer> cache = new ArrayList<>();

    static {
        cache.add(0);
        cache.add(1);
    }

    public static int solution(int n) {
        if (cache.size() > n) {
            return cache.get(n);
        } else {
            int result = solution(n - 1) + solution(n - 2);
            cache.add(n, result);
            return result;
        }
    }

    public static int solution2(int n) {
        int cacheSize = cache.size();
        while (cacheSize <= n) {
            int result = cache.get(cacheSize - 1) + cache.get(cacheSize - 2);
            cache.add(cacheSize++, result);
        }

        return cache.get(n);
    }
}
