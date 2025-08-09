package org.angelfg;

import lombok.extern.java.Log;
import org.angelfg.pipelines.PipelineAllComments;
import org.angelfg.pipelines.PipelineSumAllPricesInDiscount;
import org.angelfg.pipelines.PipelineToSelling;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log
public class Main {

    public static void main(String[] args) {
        // basesMono();
        // basesFlux();

        // pipelines();

        combinacionFlujos();

    }

    private static void combinacionFlujos() {
        Flux<String> fluxA = Flux.just("1", "2"); // From reactive mongo
        Flux<String> fluxB = Flux.just("A", "B", "C"); // From Webclient

        Flux<String> combinedFlux = fluxA.flatMap(
                strA -> fluxB.map(strB -> strA + "-" + strB)
        );

        //combinedFlux.subscribe(System.out::println);

        combinedFlux
                .map(String::toLowerCase)
                .doOnNext(System.out::println)
                .subscribe();
    }

    private static void pipelines() {
        PipelineToSelling.getTopSellingVideogames()
                .subscribe(v -> System.out.println(v));

        PipelineSumAllPricesInDiscount.getSumAllPricesInDiscount()
                .subscribe(v -> System.out.println(v));

        PipelineAllComments.getAllReviewsComments()
                .subscribe(c -> System.out.println(c));
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