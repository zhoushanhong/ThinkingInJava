package com.leetcode;

// 876 链表的中间节点
public class MiddleNode {
    public static void main(String[] args) {
        ListNode head = constructList();
        ListNode node = solution(head);
        System.out.println(node.val);
    }

    public static ListNode solution(ListNode head) {
        if (null == head) return null;

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    public static ListNode constructList() {
//        ListNode node5 = new ListNode(5, null);
        ListNode node4 = new ListNode(4, null);
        ListNode node3 = new ListNode(3, node4);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);

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
