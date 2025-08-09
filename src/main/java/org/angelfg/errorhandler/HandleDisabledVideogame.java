package org.angelfg.errorhandler;

import org.angelfg.database.Database;
import org.angelfg.models.Console;
import org.angelfg.models.Videogame;
import reactor.core.publisher.Flux;

import java.util.List;

public class HandleDisabledVideogame {

    private static final Videogame DEFAULT_VIDEOGAME =
            Videogame.builder()
                    .name("Default")
                    .price(0.0)
                    .console(Console.ALL)
                    .reviews(List.of())
                    .officialWebsite("https://www.default.com")
                    .isDiscount(true)
                    .totalSold(0)
                    .build();

    public static Flux<Videogame> handleDisabledVideogames() {
        return Database
                .getVideogamesFlux()
                .handle((vg, sink) -> {

                    if (Console.DISABLED == vg.getConsole()) {
                        sink.error(new RuntimeException("Videogame is disabled"));
                        return;
                    }

                    sink.next(vg);
                })
                .onErrorResume(error -> { // Alternativa (operacion datos de respaldo)
                    System.out.println("Error detected: " + error.getMessage());

                    // return Database.fluxAssassinsDefault;
                    return Flux.merge(
                            Database.getVideogamesFlux(),
                            Database.fluxAssassinsDefault
                    );
                })
                .cast(Videogame.class)
                .distinct(Videogame::getName); // omite
    }

    public static Flux<Videogame> handleDisabledVideogamesDefault() {
        return Database
                .getVideogamesFlux()
                .handle((vg, sink) -> {

                    if (Console.DISABLED == vg.getConsole()) {
                        sink.error(new RuntimeException("Videogame is disabled"));
                        return;
                    }

                    sink.next(vg);
                })
                .onErrorReturn(DEFAULT_VIDEOGAME) // Regresa un dato de respaldo o estatico
                .cast(Videogame.class);
    }


}
