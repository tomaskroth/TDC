package com.waes.tdc.server;

import org.junit.Test;
import reactor.core.publisher.Mono;

public class MonoExplanation {

    @Test
    public void thisIsAMono() {
        Mono<String> hello = Mono.just("Hello");

        hello.subscribe(System.out::println);
    }
}
