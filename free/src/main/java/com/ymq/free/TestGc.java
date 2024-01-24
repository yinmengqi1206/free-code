package com.ymq.free;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinmengqi
 * @description
 * @date 2024/1/24 18:10
 */
public class TestGc {

    public static void main(String[] args) throws InterruptedException {
        //-Xms50m -Xmx50m -XX:+PrintGC
        // 5MB
        int dataSize = 1024 * 1024;
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            byte[] data = new byte[dataSize];
            list.add(data);
            // 这里可以对data进行操作，例如填充数据等
            // ...
            System.out.println(STR."---------------\{data.length}");
            // 在每次循环结束后，将数组设置为null
            //list.set(i, null);
        }
    }
}
