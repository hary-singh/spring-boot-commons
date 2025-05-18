package dev.harryy.common.config;

import dev.harryy.common.test.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests to verify that the HealthProbesConfig properly configures Kubernetes-friendly health probes.
 */
@SpringBootTest(classes = {TestConfig.class, HealthProbesConfig.class})
@TestPropertySource(properties = {
        "management.health.livenessState.enabled=true",
        "management.health.readinessState.enabled=true"
})
class HealthProbesConfigTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationAvailability applicationAvailability;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private HealthProbesConfig.HealthStateController healthStateController;

    @Test
    void shouldRegisterHealthStateController() {
        assertNotNull(healthStateController, "HealthStateController should be registered");
    }

    @Test
    void shouldTrackLivenessState() {
        // Application starts in the CORRECT state
        assertEquals(LivenessState.CORRECT, applicationAvailability.getLivenessState());

        // Test state change using the controller
        healthStateController.markUnhealthy();
        assertEquals(LivenessState.BROKEN, applicationAvailability.getLivenessState());

        // Reset state for other tests
        healthStateController.markHealthy();
        assertEquals(LivenessState.CORRECT, applicationAvailability.getLivenessState());
    }

    @Test
    void shouldTrackReadinessState() {
        // Application starts in the ACCEPTING_TRAFFIC state
        assertEquals(ReadinessState.ACCEPTING_TRAFFIC, applicationAvailability.getReadinessState());

        // Test state change using the controller
        healthStateController.markNotReady();
        assertEquals(ReadinessState.REFUSING_TRAFFIC, applicationAvailability.getReadinessState());

        // Reset state for other tests
        healthStateController.markReady();
        assertEquals(ReadinessState.ACCEPTING_TRAFFIC, applicationAvailability.getReadinessState());
    }

    @Test
    void shouldHandleAvailabilityEvents() {
        // Publish an event directly to test the event listeners
        AvailabilityChangeEvent.publish(context, LivenessState.BROKEN);
        assertEquals(LivenessState.BROKEN, applicationAvailability.getLivenessState());

        // Reset state
        AvailabilityChangeEvent.publish(context, LivenessState.CORRECT);
    }
}
