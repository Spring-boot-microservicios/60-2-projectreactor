package org.angelfg.notification_system.service;

import lombok.extern.slf4j.Slf4j;
import org.angelfg.notification_system.models.NotificationEvent;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class PhoneService implements NotificacionService {

    @Override
    public Mono<Boolean> sendNotification(NotificationEvent event) {
        // Multitreahd en segundo plano fromCallable
        return Mono.fromCallable(() -> {
            Thread.sleep(1000);

            // Simular error con 20% de probabilidad
            if (ThreadLocalRandom.current().nextInt(100) < 20) {
                throw new RuntimeException("Error on send message in Phone call");
            }

            log.info("Message in Phone OK: {}", event);

            return true;
        });
    }

}
