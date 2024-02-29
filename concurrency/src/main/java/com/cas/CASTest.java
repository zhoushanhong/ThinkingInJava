package com.cas;
import com.common.UnsafeFactory;
import sun.misc.Unsafe;

/*
 * CAS缺陷
 * 长时间自旋不成功 给CPU带来大量的开销
 * 只能保证一个共享变量的原子操作
 * ABA问题
 */
public class CASTest {
    public static void main(String[] args) {
        Entity entity = new Entity();

        Unsafe unsafe = UnsafeFactory.getUnsafe();

        long offset = UnsafeFactory.getFieldOffset(unsafe, Entity.class, "x");
        System.out.println("offset: " + offset);
        boolean successful;

        // 4个参数分别是：对象实例、字段的内存偏移量、字段期望值、字段更新值
        successful = unsafe.compareAndSwapInt(entity, offset, 0, 3);
        System.out.println(successful + "\t" + entity.x);

        successful = unsafe.compareAndSwapInt(entity, offset, 3, 5);
        System.out.println(successful + "\t" + entity.x);

        successful = unsafe.compareAndSwapInt(entity, offset, 3, 8);
        System.out.println(successful + "\t" + entity.x);
    }

    private static class Entity{
        private int x;
    }
}
