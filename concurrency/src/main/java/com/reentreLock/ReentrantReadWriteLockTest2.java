package com.reentreLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest2 {
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public void processData() {
        // 锁降级从写锁获取到开始
        w.lock();
        try {
            // 写入数据
            r.lock();
        } finally {
            w.unlock();

        }

        // 锁降级完成，写锁降级为读锁

        try {
            // 使用数据
        } finally {
            r.unlock();
        }
    }
}
