package org.angelfg;

import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log
public class Main {

    public static void main(String[] args) {
        // basesMono();
        basesFlux();
    }

    private static void basesFlux() {
        // Publisher
        Flux<String> flux = Flux.just("Java", "Spring", "Reactor", "R2DBC")
                .doOnNext(value -> log.info("[onNext]: " + value))
                .doOnComplete(() -> log.info("[onComplete]: Success")) // Se ejecuta una unica vez, terminado todos los datos
                .doOnError(err -> log.info("[onError]: " + err.getMessage()));

        // Consumer
        flux.subscribe(
                data -> log.info("Receiving data: " + data),
                err -> log.info("Error: " + err.getMessage()),
                () -> log.info("Complete success")
        );
    }

    private static void basesMono() {
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