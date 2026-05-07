package com.ymq.free.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author yinmengqi
 * @Date 2026/1/21 15:54
 * @Version 1.0
 */
public class ArrayType {


    /**
     * 26 删除排序数组中的重复项
     * https://leetcode.cn/problems/remove-duplicates-from-sorted-array/description/?utm_source=LCUS&utm_medium=ip_redirect&utm_campaign=transfer2china
     *
     * @param nums
     * @return
     */
    public int removeDuplicates(int[] nums) {
        int length = nums.length;
        //处理特殊边界
        if (length == 0) {
            return 0;
        }
        int i = 1;
        for (int j = 1; j < length; j++) {
            if (nums[j] != nums[i - 1]) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }

    /**
     * https://leetcode.cn/problems/longest-substring-without-repeating-characters/?utm_source=LCUS&utm_medium=ip_redirect&utm_campaign=transfer2china
     *
     * @param
     * @return
     */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        Set<Character> seen = new HashSet<>();
        int left = 0;
        int maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            // 如果遇到重复字符，收缩左边界
            while (seen.contains(c)) {
                seen.remove(s.charAt(left));
                left++;
            }
            seen.add(c);
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }

    public static void main(String[] args) {
        int i = lengthOfLongestSubstring("abcabc");
    }
}
