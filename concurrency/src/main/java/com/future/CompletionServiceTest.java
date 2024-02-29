package com.future;

import java.util.concurrent.*;

public class CompletionServiceTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(10); //创建线程池
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor); //创建CompletionService

        cs.submit(() -> getPriceByS1()); //异步向电商S1询价
        cs.submit(() -> getPriceByS2()); //异步向电商S2询价
        cs.submit(() -> getPriceByS3()); //异步向电商S3询价

        //将询价结果异步保存到数据库
        for (int i = 0; i < 3; i++) {
            //从阻塞队列获取futureTask
            Integer r = cs.take().get(); // 不断的阻塞 直到结果队列当中有数据继续执行
            executor.execute(() -> save(r));
        }
        
        executor.shutdown();
    }

    private static void save(Integer price) {
        System.out.println("保存询价结果:" + price);
    }

    private static Integer getPriceByS1() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(3000);
        System.out.println("电商S1询价信息1200");
        return 1200;
    }

    private static Integer getPriceByS2() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(2000);
        System.out.println("电商S2询价信息1000");
        return 1000;
    }

    private static Integer getPriceByS3()  throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("电商S3询价信息800");
        return 800;
    }
}
