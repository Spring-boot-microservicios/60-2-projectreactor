package org.angelfg.pipelines;

import org.angelfg.database.Database;
import org.angelfg.models.Videogame;
import reactor.core.publisher.Flux;

public class PipelineToSelling {

    /**
     * return all names of videogames with have a sold > 80
     */
    public static Flux<String> getTopSellingVideogames() {
        return Database.getVideogamesFlux()
                .filter(videogame -> videogame.getTotalSold() > 80)
                .map(Videogame::getName);
    }

}
