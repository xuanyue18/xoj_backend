package com.xuanyue.xoj;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] nums = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < nums.length / 2; i++) {
            int temp = nums[i];
            nums[i] = nums[nums.length - 1 - i];
            nums[nums.length - 1 - i] = temp;
        }
        for (int num : nums) {
            System.out.print(num + " ");
        }
    }
}