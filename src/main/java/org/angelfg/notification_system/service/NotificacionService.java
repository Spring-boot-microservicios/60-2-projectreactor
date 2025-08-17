package org.angelfg.notification_system.service;

import org.angelfg.notification_system.models.NotificationEvent;
import reactor.core.publisher.Mono;

public interface NotificacionService {
    Mono<Boolean> sendNotification(NotificationEvent event);
}
