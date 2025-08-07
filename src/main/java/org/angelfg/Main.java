package org.angelfg;

import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
public class Main {

    public static void main(String[] args) {

        Mono<String> mono = Mono.just("Hello world")
                .doOnNext(value -> log.info("[onNext]: " + value));

        mono.subscribe();

    }

}