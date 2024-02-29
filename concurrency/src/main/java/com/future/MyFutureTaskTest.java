package com.future;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyFutureTaskTest<V> implements Runnable, Future {
    private Callable<V> callable; // 任务
    private V result = null; // 结果

    public MyFutureTaskTest(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            result = callable.call();
            synchronized (this){
                this.notify();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return result != null;
    }

    @Override
    public Object get() throws InterruptedException {
        if (result != null) {
            return result;
        }

        synchronized (this) {
            this.wait(); // 阻塞线程 等待任务完成后notify唤醒
        }

        return result;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        if (result != null) {
            return result;
        }

        if (timeout > 0L) {
            unit.sleep(timeout);
            if (result != null) {
                return result;
            } else {
                throw new TimeoutException();
            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        MyFutureTaskTest task = new MyFutureTaskTest(new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(3000);
                return "返回任务结果";
            }
        });

        new Thread(task).start();
        System.out.println("结果: " + task.get());
    }
}
