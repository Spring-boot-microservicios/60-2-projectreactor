package org.angelfg.pipelines;

import org.angelfg.database.Database;
import org.angelfg.models.Videogame;
import reactor.core.publisher.Mono;

public class PipelineSumAllPricesInDiscount {

    /*
     * Sum all prices of each videogame in discount
    */
    public static Mono<Double> getSumAllPricesInDiscount() {
        return Database.getVideogamesFlux()
                .filter(Videogame::getIsDiscount)
                .map(Videogame::getPrice)
                .reduce(Double::sum);
    }

}
