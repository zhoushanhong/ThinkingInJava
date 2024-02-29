package com.leetcode;

// 141 环形链表
public class HasCycle {
    public static void main(String[] args) {
        ListNode head = constructList();
        boolean result = solution(head);
        System.out.println(result);
    }

    public static boolean solution(ListNode head) {
        if (null == head) return false;

        ListNode slow = head.next;
        if (null == slow) {
            return false;
        }

        ListNode fast = head.next.next;

        while (null != fast && null != slow) {
            if (fast == slow) {
                return true;
            }

            slow = slow.next;
            if (null != fast.next) {
                fast = fast.next.next;
            } else {
                return false;
            }
        }

        return false;
    }

    public static ListNode constructList() {
        ListNode node4 = new ListNode(-4, null);
        ListNode node3 = new ListNode(0, node4);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(3, node2);

        node4.next = node2;

        return node1;
    }

    private static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
