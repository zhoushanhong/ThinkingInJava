package com.visibility;

// 可见性测试案例
// 线程B 控制 线程A 的执行

// jmm内存模型 线程间共享内存模型
// CPU -- 本地内存(缓存) -- 主内存

import com.common.UnsafeFactory;

// 内存屏障的理解
// 缓存强制刷新
// 底层是汇编lock前缀 保证当前缓存立刻刷回主存 并通知变量的其它副本失效(缓存过期)
public class VisibilityTest {
    // volatile在hotspot中的实现
    // bytecodeInterpreter.cpp
    private boolean flag = true; // 方法 1 volatile修饰 内存屏障
    // 方法 6 volatile修饰
    // 方法 7 Integer 最终count的值是final修饰的 final也会保证可见性
    private int count = 0;

    public static void main(String[] args) throws Exception {
        VisibilityTest test = new VisibilityTest();

        Thread threadA = new Thread(() -> test.load(), "Thread A");
        threadA.start();

        Thread.sleep(1000);

        Thread threadB = new Thread(() -> test.refresh(), "Thread B");
        threadB.start();
    }

    public void refresh() {
        // 线程B对flag的写操作会happens-before 线程A对flag的读操作
        flag = false;
        System.out.println(Thread.currentThread().getName() + " modify flag: " + flag);
    }

    // 可见性的实现方式
    // 内存屏障
    // 上下文切换
    public void load() {
        System.out.println(Thread.currentThread().getName() + " start...");

        while (flag) {
            count++;

            // 方法 2 内存屏障
            UnsafeFactory.getUnsafe().storeFence();

            // 方法 3 上下文切换(缓存过期) 从主存加载最新的值
//            Thread.yield(); // 释放时间片

            // 方法 4 依赖synchronized语义 底层是内存屏障
//            System.out.println(count);

            // 方法 5 内存屏障
//            LockSupport.unpark(Thread.currentThread());

            // 方法 6 内存屏障
//            try {
//                Thread.sleep(1);
//            } catch (Exception e) {
//                // do nothing
//            }
        }

        System.out.println(Thread.currentThread().getName() + "跳出循环: count=" + count);
    }
}
