package com.ymq.free;

/**
 * @author yinmengqi
 * @date 2024/1/5 15:13
 */
public class JDK21Test {

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            String name = "hahah";
            String message = STR."Greetings \{name}!";
            System.out.println(STR."Hello, \{message}");
        };
        // 使用静态构建器方法
        Thread virtualThread = Thread.startVirtualThread(runnable);
        Thread.sleep(3000);
    }
}
