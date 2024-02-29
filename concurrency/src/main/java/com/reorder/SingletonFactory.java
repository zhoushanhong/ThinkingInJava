package com.reorder;

public class SingletonFactory {
    private volatile static SingletonFactory instance; // 一定要用volatile修饰

    public static SingletonFactory getInstance() {
        if (null == instance) {
            synchronized (SingletonFactory.class) {
                if (null == instance) {
                    // 开辟内存空间
                    // instance指向开辟的内存空间

                    // 对象初始化
                    instance = new SingletonFactory(); // new操作有指令重排的问题
                }
            }
        }

        return instance; // 可能返回一个没有初始化完成的对象
    }

    public static void main(String[] args) {
        SingletonFactory.getInstance();
    }
}
