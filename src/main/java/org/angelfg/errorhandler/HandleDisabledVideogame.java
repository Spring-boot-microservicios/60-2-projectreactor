package org.angelfg.errorhandler;

import org.angelfg.database.Database;
import org.angelfg.models.Console;
import org.angelfg.models.Videogame;
import reactor.core.publisher.Flux;

public class HandleDisabledVideogame {

    public static Flux<Videogame> handleDisabledVideogames() {
        return Database
                .getVideogamesFlux()
                .handle((vg, sink) -> {

                    if (Console.DISABLED == vg.getConsole()) {
                        sink.error(new RuntimeException("Videogame is disabled"));
                        return;
                    }

                    sink.next(vg);
                });
    }

}
