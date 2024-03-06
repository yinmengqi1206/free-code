package com.ymq.free.completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yinmengqi
 * @description
 * @date 2024/3/6 17:03
 */
public class BinaryDependency {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CompletableFuture<String> step1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行【步骤1】");
            return "【步骤1】执行结果";
        }, executor);
        CompletableFuture<String> step2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行【步骤2】");
            return "【步骤2】执行结果";
        }, executor);
        CompletableFuture<String> step3 = step1.thenCombine(step2, (result1, result2) -> {
            System.out.println("前两步操作结果分别为：" + result1 + result2);
            return "【步骤3的执行结果】";
        });
        System.out.println("步骤3的执行结果：" + step3.get());
    }
}
