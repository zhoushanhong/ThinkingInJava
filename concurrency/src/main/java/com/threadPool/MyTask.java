package com.threadPool;

public class MyTask implements Runnable {
    private final int taskId;

    public MyTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "-程序员做第" + taskId + "个项目");

        try {
            Thread.sleep(3000); // 实际业务逻辑
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
