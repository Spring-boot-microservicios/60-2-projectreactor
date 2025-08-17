package org.angelfg.notification_system;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.angelfg.notification_system.models.NotificationEvent;
import org.angelfg.notification_system.service.EmailService;
import org.angelfg.notification_system.service.NotificacionService;
import org.angelfg.notification_system.service.PhoneService;
import org.angelfg.notification_system.service.TeamService;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Getter
public class NotificationSystem {

    // Emite a muchos canales
    private final Sinks.Many<NotificationEvent> mainEventSink;
    private final Sinks.Many<NotificationEvent> historySink;

    private final NotificacionService teamsService;
    private final NotificacionService emailService;
    private final NotificacionService phoneService;

    // Emite un solo canal
    private final Sinks.One<NotificationEvent> teamsSink;
    private final Sinks.One<NotificationEvent> emailSink;
    private final Sinks.One<NotificationEvent> phoneSink;

    private final ConcurrentMap<String, NotificationEvent> notificationCache;

    public NotificationSystem() {
        // onBackpressureBuffer evita a que se sature mi servicio
        this.mainEventSink = Sinks.many().multicast().onBackpressureBuffer();

        this.historySink = Sinks.many().replay().limit(50); // Ultimos 50

        this.teamsSink = Sinks.one();
        this.emailSink = Sinks.one();
        this.phoneSink = Sinks.one();

        this.teamsService = new TeamService();
        this.emailService = new EmailService();
        this.phoneService = new PhoneService();

        this.notificationCache = new ConcurrentHashMap<>();
    }

}
