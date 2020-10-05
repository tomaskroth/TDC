package com.tk.tdc.server;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class FluxExplanation {
    private static List<String> words = Arrays.asList(
            "the",
            "quick",
            "brown",
            "foxes",
            "jumped",
            "over",
            "the",
            "lazy",
            "dog"
    );

    @Test
    public void simpleCreation() {
        Flux<String> fewWords = Flux.just("Hello", "World");
        Flux<String> manyWords = Flux.fromIterable(words);

        fewWords.subscribe(System.out::println);
        System.out.println();
        manyWords.subscribe(System.out::println);
    }

    @Test
    public void streamLetterByLetter() {
        Flux<String> manyLetters = Flux.fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .distinct()
                .sort()
                .zipWith(
                        Flux.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string)
                );


        manyLetters.subscribe(System.out::println);

    }

    @Test
    public void streamLetterByLetter2() {
        Flux<String> manyWords = Flux.just(1, 2, 3, 4)
                .zipWith(
                        Flux.fromIterable(words)
                                .flatMap(word -> Flux.fromArray(word.split("")))
                                .distinct()
                                .sort(),
                        (count, string) -> String.format("%2d. %s", count, string)
                );


        manyWords.subscribe(System.out::println);

    }

    @Test
    public void monoPlusMonoEqualsFlux() {

        Flux<String> twoMonos = Mono.just("Hello").concatWith(Mono.just("World"));

        twoMonos.subscribe(System.out::println);


    }


}
