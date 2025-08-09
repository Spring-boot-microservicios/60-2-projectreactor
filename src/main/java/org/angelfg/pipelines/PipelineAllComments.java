package org.angelfg.pipelines;

import org.angelfg.database.Database;
import org.angelfg.models.Review;
import reactor.core.publisher.Flux;

public class PipelineAllComments {

    /**
     * box -> [items, items, items], box -> [items, items]
     * .map box -> boxWithLabel = 2 items
     * .flatMap: item -> userItem(item) // 5 elementos (aplana los arreglos en uno)
     */
    public static Flux<String> getAllReviewsComments() {
        return Database
                .getVideogamesFlux()
                .flatMap(videogame -> Flux.fromIterable(videogame.getReviews())) // videogames -> reviews
                .map(Review::getComment); // Referencia a metodos
    }

}
