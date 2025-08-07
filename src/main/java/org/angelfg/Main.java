package org.angelfg;

import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
public class Main {

    public static void main(String[] args) {

        // Publisher
        Mono<String> mono = Mono.just("Hello world")
                .doOnNext(value -> log.info("[onNext]: " + value))
                .doOnSuccess(value -> log.info("[onSucces]: " + value))
                .doOnError(err -> log.info("[onError]: " + err.getMessage()));

        // Consumer
        mono.subscribe(
                data -> log.info("Receiving data: " + data),
                err -> log.info("Error: " + err.getMessage()),
                () -> log.info("Complete success")
        );

        // Consumer (Puedo usar tanto como los necesite)
//        mono.subscribe(
//                data -> log.info("Receiving data: " + data)
//        );

    }

}