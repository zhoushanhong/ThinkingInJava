package com.synchronize;

import com.common.UnsafeFactory;
import sun.misc.Unsafe;

import java.nio.ByteOrder;

/*
 * 判断当前环境字节是大端还是小端字节序存储
 * Little-Endian 高位字节在前，低位字节在后。
 * Big-Endian 低位字节在前，高位字节在后。
 * 在x86的计算机中，一般采用的是小端字节序
 */
public class EndianTest {
    public static void main(String[] args) {
        Unsafe unsafe = UnsafeFactory.getUnsafe();
        long alloc = unsafe.allocateMemory(8);
        try {
            unsafe.putLong(alloc, 0x0102030405060708L);
            byte b = unsafe.getByte(alloc);
            ByteOrder byteOrder;
            switch (b) {
                case 0x01: byteOrder = ByteOrder.BIG_ENDIAN;     break;
                case 0x08: byteOrder = ByteOrder.LITTLE_ENDIAN;  break;
                default:
                    byteOrder = null;
            }
            System.out.println(byteOrder);
        } finally {
            unsafe.freeMemory(alloc);
        }
    }
}
