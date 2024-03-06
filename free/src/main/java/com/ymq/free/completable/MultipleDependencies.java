package com.ymq.free.completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yinmengqi
 * @description
 * @date 2024/3/6 17:16
 */
public class MultipleDependencies {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CompletableFuture<String> step1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行【步骤1】");
            return "【步骤1的执行结果】";
        }, executor);
        CompletableFuture<String> step2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行【步骤2】");
            return "【步骤2的执行结果】";
        }, executor);
        CompletableFuture<String> step3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行【步骤3】");
            return "【步骤3的执行结果】";
        }, executor);

        CompletableFuture<Void> stepM = CompletableFuture.allOf(step1, step2, step3);
        CompletableFuture<String> stepMResult = stepM.thenApply(res -> {
            // 通过join函数获取返回值
            String result1 = step1.join();
            String result2 = step2.join();
            String result3 = step3.join();

            return result1 + result2 + result3;
        });
        System.out.println("步骤M的结果：" + stepMResult.get());

    }
}
