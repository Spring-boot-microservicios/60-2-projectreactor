package org.angelfg;

import lombok.extern.slf4j.Slf4j;
import org.angelfg.callbacks.CallbacksExample;
import org.angelfg.database.Database;
import org.angelfg.errorhandler.FallbackService;
import org.angelfg.errorhandler.HandleDisabledVideogame;
import org.angelfg.models.Console;
import org.angelfg.models.Videogame;
import org.angelfg.pipelines.PipelineAllComments;
import org.angelfg.pipelines.PipelineSumAllPricesInDiscount;
import org.angelfg.pipelines.PipelineToSelling;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Duration;

@Slf4j
public class Main {

    public static void main(String[] args) {
        // basesMono();
        // basesFlux();
        // pipelines();
        // combinacionFlujos();
        // operadorConcatAndMerge();
        // tuplas();

//        HandleDisabledVideogame.handleDisabledVideogames()
//                .subscribe(System.out::println);

//        HandleDisabledVideogame.handleDisabledVideogamesDefault()
//                .subscribe(v -> log.info(v.toString()));

//        FallbackService.callFallback()
//                .subscribe(v -> log.info(v.toString()));

        // callbacks();

        // User 1 = XBOX, User 2 = PLAYSTATION (recomendaciones en base al usuario)
        // Contexto

        Database.getVideogamesFlux()
                // Es asincrono el filter normal no
                .filterWhen(videogame -> Mono.deferContextual(ctx -> {

                    var userId = ctx.getOrDefault("userId", "0");

                    if (userId.startsWith("1")) {
                        return Mono.just(videogameForConsole(videogame, Console.XBOX));
                    } else if (userId.startsWith("2")) {
                        return Mono.just(videogameForConsole(videogame, Console.PLAYSTATION));
                    }

                    return Mono.just(false);
                }))
                .contextWrite(Context.of("userId", "10020192")) // Contexto siempre es antes del subscribe
                .subscribe(videogame -> log.info("Recommended name {} console {}", videogame.getName(), videogame.getConsole()));

    }

    public static boolean videogameForConsole(Videogame videogame, Console console) {
        return videogame.getConsole() == console || videogame.getConsole() == Console.ALL;
    }

    private static void callbacks() {
        // CALLBACKS
        CallbacksExample
                .callbacks()
                .subscribe(
                        data -> log.debug(data.getName()), // Se basa en onNext
                        err -> log.error(err.getMessage()), // Se ejecuta en base a onError
                        () -> log.debug("Finish subs") // Se ejecuta en base onFinally
                ); // Se ejecuta desde la subscripcion y doOnNext dentro de la emision

//        CallbacksExample
//                .callbacks()
//                .blockLast();
    }

    private static void tuplas() {
        // Funciona en tupla
        Flux<String> fluxProducts = Flux.just("Product1", "Product2", "Product3").delayElements(Duration.ofMillis(120));
        Flux<String> fluxWarehouse = Flux.just("Stock1", "Stock2", "Stock3").delayElements(Duration.ofMillis(50));
        Flux<String> fluxPayments = Flux.just("Pay1", "Pay2", "Pay3").delayElements(Duration.ofMillis(150));
        Flux<String> fluxSuccess = Flux.just("Success1", "Success2", "Success3").delayElements(Duration.ofMillis(20));

        Flux<String> reportFlux = Flux.zip(fluxProducts, fluxWarehouse, fluxPayments, fluxSuccess)
                        .map(tuple ->
                                tuple.getT1() + " " +
                                tuple.getT2() + " " +
                                tuple.getT3() + " " +
                                tuple.getT4()
                        );

        reportFlux
                .doOnNext(System.out::println)
                .blockLast();
    }

    private static void operadorConcatAndMerge() {
        // Scheduling -> .delayElements(Duration.ofMillis(100));
        Flux<String> fluxA = Flux.just("1", "2").delayElements(Duration.ofMillis(100)); // From reactive mongo
        Flux<String> fluxB = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(50)); // From Webclient

        Flux<String> combinedFlux1 = Flux.merge(fluxA, fluxB); // Combino elementos (En scheduling los pone segun termine)
        Flux<String> combinedFlux2 = Flux.concat(fluxA, fluxB); // Pega los flujos (En scheduling se espera a que terminen y los acomoda)

        combinedFlux1
                .doOnNext(System.out::println)
                .blockLast();
        //.subscribe(System.out::println);

        System.out.println();
        combinedFlux2
                .doOnNext(System.out::println)
                .blockLast();
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