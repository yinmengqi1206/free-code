package com.ymq.free;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2023/6/26 10:20
 */
public class ThreadLoopPrint {

    /**
     * 3 个线程交替打印 ABC
     */
    // 共享计数器
    private static int sharedCounter = 0;

    public static void main(String[] args) {
        cyclicBarrier();
    }

    public static void cyclicBarrier(){
        // 打印的内容
        String printString = "ABC";
        // 定义循环栅栏
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
        });
        // 执行任务
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < printString.length(); i++) {
                    synchronized (this) {
                        sharedCounter = sharedCounter > 2 ? 0 : sharedCounter; // 循环打印
                        System.out.println(Thread.currentThread().getName() + ":" + printString.toCharArray()[sharedCounter++]);
                    }
                    try {
                        // 等待 3 个线程都打印一遍之后，继续走下一轮的打印
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        // 开启多个线程
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
