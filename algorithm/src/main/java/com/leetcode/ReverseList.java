package com.leetcode;


// 206 反转链表
public class ReverseList {
    public static void main(String[] args) {
        ListNode head = constructList();
        printList(head);

        // 翻转链表
        ListNode newHead = reverseList(head);
        printList(newHead);

//        ListNode newHead = reverseListRecursion(head);
//        printList(newHead);
    }

    public static ListNode reverseList(ListNode head) {
        ListNode currentNode = head; // 当前节点
        ListNode currentNodePrev = null; // 当前节点的上一个节点

        while (null != currentNode) {
            ListNode temp = currentNode.next;
            currentNode.next = currentNodePrev;

            currentNodePrev = currentNode;
            currentNode = temp;
        }

        return currentNodePrev;
    }

    public static ListNode reverseListRecursion(ListNode head) {
        if (null == head || null == head.next) {
            return head;
        }

        ListNode currentNode = head;
        ListNode currentNodeNext = head.next;

        // 递归寻找最后一个节点
        ListNode newHead = reverseListRecursion(currentNodeNext);

        // 两两节点进行处理
        currentNodeNext.next = currentNode; // 后一个节点指向前一个节点
        currentNode.next = null; // 前一个节点的下一个节点指向空 避免循环链表

        return newHead; // 始终是反转后链表的头结点
    }

    public static ListNode constructList() {
        ListNode node5 = new ListNode(5, null);
        ListNode node4 = new ListNode(4, node5);
        ListNode node3 = new ListNode(3, node4);
        ListNode node2 = new ListNode(2, node3);
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
