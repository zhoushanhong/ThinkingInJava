package com.leetcode;

// 160 相交链表
public class GetIntersectionNode {
    private static ListNode nodeC3 = new ListNode(5, null);
    private static ListNode nodeC2 = new ListNode(4, nodeC3);
    private static ListNode nodeC1 = new ListNode(8, nodeC2);

    public static void main(String[] args) {
        ListNode headA = constructList1();
        ListNode headB = constructList2();

        ListNode node = solution(headA, headB);
        System.out.println(node.val);
    }

    public static ListNode solution(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;

        ListNode travelA = headA;
        ListNode travelB = headB;

        while (travelA != travelB) {
            travelA = (travelA == null) ? headB : travelA.next;
            travelB = (travelB == null) ? headA : travelB.next;
        }

        return travelA;
    }

    public static ListNode constructList1() {
        ListNode node3 = nodeC1;
        ListNode node2 = new ListNode(1, node3);
        ListNode node1 = new ListNode(4, node2);

        return node1;
    }

    public static ListNode constructList2() {
        ListNode node4 = nodeC1;
        ListNode node3 = new ListNode(1, node4);
        ListNode node2 = new ListNode(6, node3);
        ListNode node1 = new ListNode(5, node2);

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
