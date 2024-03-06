package com.ymq.free.completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://juejin.cn/post/7212466685450207290?searchId=2024030616493205F2E597B75AD00FE7EA#heading-7
 *
 * @author yinmengqi
 * @description
 * @date 2024/3/6 16:58
 */
public class UnaryDependency {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CompletableFuture<String> step1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行【步骤1】");
            return "【步骤1的执行结果】";
        }, executor);

        CompletableFuture<String> step2 = step1.thenApply(result -> {
            System.out.println("上一步操作结果为：" + result);
            return "【步骤2的执行结果】";
        });
        System.out.println("步骤2的执行结果：" + step2.get());

    }
}
