package com.waes.tdc.server;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class FlowControl {

    @Test
    public void first() {
        Mono<String> a = Mono.just("oops I'm late")
                             .delayElement(Duration.ofMillis(450));
        Flux<String> b = Flux.just("let's get", "the party", "started")
                             .delayElements(Duration.ofMillis(400));

        Flux.first(a, b)
            .toIterable()
            .forEach(System.out::println);
    }

    @Test
    public void understandingBackPressure() {

        Flux<Integer> lotsOfData = Flux.range(1, 200)
                                       .limitRate(5);
//        Flux<Integer> lotsOfData = Flux.range(1, Integer.MAX_VALUE).limitRate(5, 3);
//        Flux<Integer> lotsOfData = Flux.range(1, Integer.MAX_VALUE).limitRequest(5);

        lotsOfData
                .subscribe(x -> {
                    System.out.println(x);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

    }

    @Test
    public void itsNotThatSimple() {
        Flux<String> flux = Flux.just("let's get", "the party", "started")
                                .flatMap(x -> {
                                    System.out.println(x);
                                    return Flux.just(x);
                                });

        Flux<Object> take = flux.take(1)
                                .flatMap(x -> {
                                            System.out.println("1 ===== " + x);
                                            return Flux.just(x);
                                        }
                                );

        take.subscribe();
    }
}
