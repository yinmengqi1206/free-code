package com.ymq.free;

import reactor.core.publisher.Flux;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2023/7/14 10:23
 */
public class ReactiveTest {

    public static void main(String[] args) {
        Flux.create((t) -> {
            t.next("create");
            t.next("create1");
            t.complete();
        }).subscribe(System.out::println);
        Flux.generate(t -> {
            t.next("generate");
            //注意generate中next只能调用1次
            t.complete();
        }).subscribe(System.out::println);
        Flux.just("just1","just2").subscribe(System.out::println);
    }
}
