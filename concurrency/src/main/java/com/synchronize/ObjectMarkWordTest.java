package com.synchronize;

import org.openjdk.jol.info.ClassLayout;

/*
 * 启动4s之后创建的对象都是带偏向锁 但没有threadId
 * 调用hashCode后 偏向锁变无锁 因为偏向锁的Mark Word的数据结构是无法存储hashCode的
 * 多线程竞争锁后 偏向锁升级成轻量级锁
 * 竞争过后 轻量级锁也不会退化成偏向锁
 */
public class ObjectMarkWordTest {
    public static void main(String[] args) throws InterruptedException {
        //jvm延迟偏向
        Thread.sleep(5000);
//        Object obj = new Integer[4];

        Object obj = new Model();
//        int hashCode = obj.hashCode();
//        System.out.println("hashCode: " + Integer.toBinaryString(hashCode));
        //查看对象内部信息
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());

        new Thread(()->{
            synchronized (obj){
//                obj.notify(); // 调用wait/notify 会升级成轻量级锁

                try {
                    obj.wait(10); // 调用wait(time) 会升级成重量级锁
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "\n" + ClassLayout.parseInstance(obj).toPrintable());
            }
            System.out.println(Thread.currentThread().getName() + "释放锁\n" + ClassLayout.parseInstance(obj).toPrintable());

            // jvm 优化
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"Thread-1").start();

        Thread.sleep(3000);

        new Thread(()->{
            synchronized (obj){
                System.out.println(Thread.currentThread().getName() + "\n" + ClassLayout.parseInstance(obj).toPrintable());
            }
        },"Thread-2").start();
    }

    private static class Model {
        private boolean flag;
        private long p;
    }
}

