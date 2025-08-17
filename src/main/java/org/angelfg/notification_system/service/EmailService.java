package org.angelfg.notification_system.service;

import lombok.extern.slf4j.Slf4j;
import org.angelfg.notification_system.models.NotificationEvent;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class EmailService implements NotificacionService {

    @Override
    public Mono<Boolean> sendNotification(NotificationEvent event) {
        // Multitreahd en segundo plano fromCallable
        return Mono.fromCallable(() -> {
            Thread.sleep(300);

            // Simular error con 15% de probabilidad
            if (ThreadLocalRandom.current().nextInt(100) < 15) {
                throw new RuntimeException("Error on send message in Email");
            }

            log.info("Message in Email OK: {}", event);

            return true;
        });
    }

}
