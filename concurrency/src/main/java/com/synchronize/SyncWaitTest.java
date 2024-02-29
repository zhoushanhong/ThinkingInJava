package com.synchronize;

/*
 * wait会释放锁 但是sleep不会
 */
public class SyncWaitTest {
    private Object lock = new Object();

    public static void main(String[] args) {
        SyncWaitTest t = new SyncWaitTest();

        for(int i = 0; i < 2; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    t.process();
                }
            },"thread-" + i).start();
        }

    }

    public void process() {
        System.out.println(Thread.currentThread().getName()+" start");

        synchronized (lock){
            System.out.println(Thread.currentThread().getName()+" execute");
            try {
                Thread.sleep(5000);
//                lock.wait(5000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" end");
        }

    }
}
