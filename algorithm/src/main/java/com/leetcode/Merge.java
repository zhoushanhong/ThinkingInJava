package com.leetcode;

// 88 合并两个有序数组
public class Merge {
    public static void main(String[] args) {
        int[] nums1 = new int[] {1,2,3,0,0,0};
        int m = 3;

        int[] nums2 = new int[] {2,5,6};
        int n = 3;

        solution(nums1, m, nums2, n);
        System.out.println("just for break");
    }

    public static void solution(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) {
            return;
        }

        int mIndex = m - 1;
        int nIndex = n - 1;
        int index = m + n - 1;

        try {
            while (index >= 0) {
                if (mIndex < 0) {
                    nums1[index] = nums2[nIndex];
                    nIndex--;
                    index--;
                    continue;
                }

                if (nIndex < 0) {
                    nums1[index] = nums1[mIndex];
                    mIndex--;
                    index--;
                    continue;
                }

                if (nums1[mIndex] > nums2[nIndex]) {
                    nums1[index] = nums1[mIndex];
                    mIndex--;
                } else {
                    nums1[index] = nums2[nIndex];
                    nIndex--;
                }

                index--;
            }
        } catch (Exception e) {
            printArray(nums1);
            System.out.println("index: " + index + ", mIndex: " + mIndex + ", nIndex: " + nIndex);
        }
    }

    private static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
    }
}
