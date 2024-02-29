package com.leetcode;

// 83 删除排序链表中的重复元素
public class DeleteDuplicates {
    public static void main(String[] args) {
        ListNode head = constructList();
        printList(head);

        head = solution(head);
        printList(head);
    }

//    public static ListNode solution(ListNode head) {
//        if(null == head) return head;
//
//        ListNode currentNode = head;
//        while (null != currentNode.next) {
//            if (currentNode.val == currentNode.next.val) {
//                currentNode.next = currentNode.next.next;
//            } else {
//                currentNode = currentNode.next;
//            }
//        }
//
//        return head;
//    }

    public static ListNode solution(ListNode head) {
        if(null == head) return head;

        ListNode slow = head;
        ListNode fast = head.next;

        while (null != fast) {
            if (slow.val == fast.val) {
                slow.next = fast.next;
                fast = fast.next;
            } else {
                slow = slow.next;
                fast = fast.next;
            }
        }

        return head;
    }

    public static ListNode constructList() {
        ListNode node5 = new ListNode(3, null);
        ListNode node4 = new ListNode(3, node5);
        ListNode node3 = new ListNode(2, node4);
        ListNode node2 = new ListNode(1, node3);
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
