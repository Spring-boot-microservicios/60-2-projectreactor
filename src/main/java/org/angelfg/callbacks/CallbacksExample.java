package org.angelfg.callbacks;

import lombok.extern.slf4j.Slf4j;
import org.angelfg.database.Database;
import org.angelfg.models.Videogame;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class CallbacksExample {

    public static Flux<Videogame> callbacks() {
        return Database
                .getVideogamesFlux()

                // Generar error
                //.delayElements(Duration.ofMillis(500)) // Hacer latencia cada elemento
                //.timeout(Duration.ofMillis(300)) // Si se tarda manda error

                // Primero llama doOnSubscribe
                .doOnSubscribe(subs -> log.info("[doOnSubscribe]"))

                // Numero de elementos
                .doOnRequest(n -> log.info("[doOnRequest]: {}", n))

                // Emite el siguiente elemento
                .doOnNext(videogame -> log.info("[doOnNext]: {}", videogame))

                // Cancelar elemento
                .doOnCancel(() -> log.warn("[doOnCancel]"))

                // Lanza una excepcion o error throwable
                .doOnError(err -> log.error("[doOnError]: {}", err.getMessage()))

                // Se ejecuta unicamente si todo sale bien
                .doOnComplete(() -> log.info("[doOnComplete]: success"))

                // Siempre se ejecuta aunque el flujo se cancela o sale error tipo try/catch
                .doOnTerminate(() -> log.info("[doOnTerminate]"))

                // Siempre se ejecuta similar a doOnTerminate pero dice como termino
                .doFinally(signal -> log.warn("[doFinally]: {}", signal));
    }

}
