package com.tk.tdc.server;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<Long> rebatchedRequest = Collections.synchronizedList(new ArrayList<>());

        Flux<Integer> lotsOfData = Flux.range(1, 1000).doOnRequest(rebatchedRequest::add)

                .limitRate(100);

        StepVerifier.create(lotsOfData, 1000).expectNextCount(1000).verifyComplete();

        Assertions.assertThat(rebatchedRequest)
                //14 requests to completion
                .containsExactly(100L, 75L, 75L, 75L, 75L, 75L, 75L, 75L, 75L, 75L, 75L, 75L, 75L, 75L);

    }

    @Test
    public void limitRateWithVeryLowTide() {
        //Low-tide pre-fetch rate
        List<Long> rebatchedRequest = Collections.synchronizedList(new ArrayList<>());
        final Flux<Integer> test = Flux
                .range(1, 14)
                .hide()
                .doOnRequest(rebatchedRequest::add)

                .limitRate(10,2);


        StepVerifier.create(test, 14)
                .expectNextCount(14)
                .verifyComplete();

        Assertions.assertThat(rebatchedRequest)
                .containsExactly(10L, 2L, 2L, 2L, 2L, 2L, 2L, 2L);
    }

    @Test
    public void limitRateWithCloseLowTide() {
        List<Long> rebatchedRequest = Collections.synchronizedList(new ArrayList<>());
        final Flux<Integer> test = Flux
                .range(1, 14)
                .hide()
                .doOnRequest(rebatchedRequest::add)
                .limitRate(10,8);
        StepVerifier.create(test, 14)
                .expectNextCount(14)
                .verifyComplete();
        Assertions.assertThat(rebatchedRequest)
                .containsExactly(10L, 8L);
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
