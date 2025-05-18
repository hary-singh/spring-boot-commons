package dev.harryy.common.config;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.logging.Logger;

/**
 * Configures health probes for microservices, particularly for Kubernetes environments.
 * Provides both liveness and readiness probes via Spring Boot Actuator's health endpoints.
 */
@Configuration
public class HealthProbesConfig {

    private static final Logger logger = Logger.getLogger(HealthProbesConfig.class.getName());

    /**
     * Monitors the application's liveness state and logs state changes.
     * The liveness probe verifies the application is running and hasn't deadlocked.
     */
    @EventListener
    public void onLivenessStateChange(AvailabilityChangeEvent<LivenessState> event) {
        logger.info("Liveness state changed to: " + event.getState());
    }

    /**
     * Monitors the application's readiness state and logs state changes.
     * The readiness probe verifies the application is ready to accept traffic.
     */
    @EventListener
    public void onReadinessStateChange(AvailabilityChangeEvent<ReadinessState> event) {
        logger.info("Readiness state changed to: " + event.getState());
    }

    /**
     * Bean to provide a simple way to manually change application availability states if needed.
     */
    @Bean
    public HealthStateController healthStateController(ApplicationContext context) {
        return new HealthStateController(context);
    }

    /**
     * Helper class that provides methods to manually change the application's health states if needed.
     */
    public static class HealthStateController {
        private final ApplicationContext context;

        public HealthStateController(ApplicationContext context) {
            this.context = context;
        }

        public void markUnhealthy() {
            AvailabilityChangeEvent.publish(context, LivenessState.BROKEN);
        }

        public void markHealthy() {
            AvailabilityChangeEvent.publish(context, LivenessState.CORRECT);
        }

        public void markNotReady() {
            AvailabilityChangeEvent.publish(context, ReadinessState.REFUSING_TRAFFIC);
        }

        public void markReady() {
            AvailabilityChangeEvent.publish(context, ReadinessState.ACCEPTING_TRAFFIC);
        }
    }
}
