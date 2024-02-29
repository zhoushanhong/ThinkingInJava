package com.leetcode;

// 21 合并两个有序链表
public class MergeTwoLists {
    public static void main(String[] args) {
        ListNode list1 = constructList1();
        ListNode list2 = constructList2();

        ListNode head = solution(list1, list2);
        printList(head);
    }

    public static ListNode solution(ListNode list1, ListNode list2) {
        if (null == list1) return list2;
        if (null == list2) return list1;

        ListNode head = new ListNode(0, null);
        ListNode currentNode = head;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                currentNode.next = list1;
                list1 = list1.next;
            } else {
                currentNode.next = list2;
                list2 = list2.next;
            }

            currentNode = currentNode.next;
        }

        if (list1 == null) currentNode.next = list2;
        if (list2 == null) currentNode.next = list1;

        return head.next;
    }

    public static ListNode constructList1() {
        ListNode node3 = new ListNode(4, null);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);

        return node1;
    }

    public static ListNode constructList2() {
        ListNode node3 = new ListNode(4, null);
        ListNode node2 = new ListNode(3, node3);
        ListNode node1 = new ListNode(1, node2);

        return node1;
    }

    public static void printList(ListNode head) {
        ListNode node = head;
        while (null != node) {
            System.out.print(node.val + " ");
            node = node.next;
        }

        System.out.println();
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
