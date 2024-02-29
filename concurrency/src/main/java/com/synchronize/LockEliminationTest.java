package com.synchronize;

public class LockEliminationTest {
    /**
     * 锁消除
     * -XX:+EliminateLocks 开启锁消除(jdk8默认开启）
     * -XX:-EliminateLocks 关闭锁消除
     */
    public static void main(String[] args) throws InterruptedException {
        LockEliminationTest t = new LockEliminationTest();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            t.append("aaa", "bbb");
        }

        long end = System.currentTimeMillis();
        System.out.println("执行时间：" + (end - start) + " ms");
    }

    public void append(String str1, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str1).append(str2);
    }
}
