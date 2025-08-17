package org.angelfg.notification_system.service;

import lombok.extern.slf4j.Slf4j;
import org.angelfg.notification_system.models.NotificationEvent;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class TeamService implements NotificacionService {

    @Override
    public Mono<Boolean> sendNotification(NotificationEvent event) {
        // Multitreahd en segundo plano fromCallable
        return Mono.fromCallable(() -> {
            Thread.sleep(150);

            // Simular error con 10% de probabilidad
            if (ThreadLocalRandom.current().nextInt(10) == 0) {
                throw new RuntimeException("Error on send message in Teams");
            }

            log.info("Message in Teams OK: {}", event);

            return true;
        });
    }

}
