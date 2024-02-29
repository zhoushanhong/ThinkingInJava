package com.leetcode;

// 283 移动零
public class MoveZeros {
    public static void main(String[] args) {
        int[] nums = new int[] {0,1,0,3,12};
        solution(nums);
        printArray(nums);
    }

    public static void solution(int[] nums) {
        int j = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                continue;
            }

            nums[j] = nums[i];
            j++;
        }

        for (;j < nums.length; j++) {
            nums[j] = 0;
        }
    }

    private static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
    }
}
