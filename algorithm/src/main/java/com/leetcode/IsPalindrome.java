package com.leetcode;

// 234 回文链表
public class IsPalindrome {
    public static void main(String[] args) {
        ListNode head = constructList();
        boolean result = solution(head);
        System.out.println(result);
    }

    public static boolean solution(ListNode head) {
        if (null == head) return false;

        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        if (fast != null) {
            slow = slow.next;
        }

        slow = reverseList(slow);
        fast = head;
        while (slow != null) {
            if (fast.val != slow.val) {
                return false;
            }
            slow = slow.next;
            fast = fast.next;
        }

        return true;
    }

    public static ListNode reverseList(ListNode head) {
        ListNode currentNode = head;
        ListNode currentNodePre = null;

        while (null != currentNode) {
            ListNode temp = currentNode.next;
            currentNode.next = currentNodePre;

            currentNodePre = currentNode;
            currentNode = temp;
        }

        return currentNodePre;
    }

    public static ListNode constructList() {
        ListNode node5 = new ListNode(1, null);
        ListNode node4 = new ListNode(2, node5);
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
