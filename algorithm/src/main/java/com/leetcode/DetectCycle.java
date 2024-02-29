package com.leetcode;

// 142 环形链表2
public class DetectCycle {
    public static void main(String[] args) {
        ListNode head = constructList();
        ListNode node = solution(head);
        System.out.println(node.val);
    }

    public static ListNode solution(ListNode head) {
        if (null == head) return null;

        ListNode slow = head.next;
        if (null == slow) {
            return null;
        }

        ListNode fast = head.next.next;
        boolean hasCycle = false;

        while (null != fast && null != slow) {
            if (fast == slow) {
                hasCycle = true;
                break;
            }

            slow = slow.next;
            if (null != fast.next) {
                fast = fast.next.next;
            } else {
                return null;
            }
        }

        if (hasCycle) {
            slow = head;
            while(slow != fast) {
                slow = slow.next;
                fast = fast.next;
            }
            return slow;
        } else {
            return null;
        }
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
