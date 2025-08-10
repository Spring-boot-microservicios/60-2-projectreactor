package org.angelfg.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.angelfg.database.Database;
import org.angelfg.models.Console;
import org.angelfg.models.Videogame;
import reactor.core.publisher.Flux;

@Slf4j
public class FallbackService {

    public static Flux<Videogame> callFallback() {
        return Database
                .getVideogamesFlux()
                .handle((vg, sink) -> {

                    if (Console.DISABLED == vg.getConsole()) {
                        sink.error(new RuntimeException("Videogame is disabled"));
                        return;
                    }

                    sink.next(vg);
                })
                .retry(5) // Numero no debe ser mu grande max. 20
                .onErrorResume(error -> { // Alternativa (operacion datos de respaldo)
                    log.error("database is falling");

                    // return Flux.empty();
                    return Database.fluxFallback;
                })
                .repeat(1) // Repite todo el flujo
                .cast(Videogame.class);
    }

}
