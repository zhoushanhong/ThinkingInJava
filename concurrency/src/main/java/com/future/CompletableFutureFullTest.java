package com.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureFullTest {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * TODO: 没有线程池 也能正确处理 缺省的线程池是什么参数
     */
    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }

    /**
     * 同时(厨师)炒菜 (服务员)煮饭 (monkey)看抖音
     * 菜好了 饭好了 打饭(通过回调实现)
     * 最后吃饭
     */
    private static void test1() {
        System.out.println("monkey进入餐厅, 点了份西红柿炒番茄");
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(()->{
            System.out.println("厨师炒菜");
            sleep(2, TimeUnit.SECONDS);
            return "西红柿炒番茄好了";
        },executorService).thenCombine(CompletableFuture.supplyAsync(()->{
            System.out.println("服务员蒸饭");
            sleep(3,TimeUnit.SECONDS);
            return "米饭好了";
        }),(dish, rice)->{
            System.out.println("服务员打饭");
            sleep(1,TimeUnit.SECONDS);
            return dish + ", " + rice;
        });

        System.out.println("monkey在刷抖音");
        System.out.println(cf.join() + ", monkey开吃");
    }

    /**
     * 先收款 再开票
     */
    private static void test2() {
        System.out.println("monkey吃完饭去结账, 要求开发票");
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(()->{
            System.out.println("服务员收款");
            sleep(1,TimeUnit.SECONDS);
            return "20";
        }).thenApply(money->{
            System.out.println("服务员开发票, 面额" + money + "元");
            sleep(2,TimeUnit.SECONDS);
            return money + "元发票";
        });

        System.out.println("monkey拿到" + cf.join() + ", 准备回家");
    }

    /**
     * 301, 918那辆车先来坐那辆
     * 出现异常后 处理异常
     */
    private static void test3() {
        System.out.println("monkey走出餐厅, 来到公交车站，等待301路或者918路公交到来");
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println("301路公交正在赶来");
            sleep(2,TimeUnit.SECONDS);
            return "301路到了";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            System.out.println("918路公交正在赶来");
            sleep(1,TimeUnit.SECONDS);
            return "918路到了";
        }), bus -> {
            if(bus.startsWith("918")){
                throw new RuntimeException("918撞树了.......");
            }
            return bus;
        }).exceptionally(e->{
            System.out.println(e.getMessage());
            System.out.println("monkey叫出租车");
            sleep(3,TimeUnit.SECONDS);
            return "出租车到了";
        });

        System.out.println(cf.join() + ", monkey坐车回家");
    }

    private static void sleep(int t, TimeUnit u){
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
