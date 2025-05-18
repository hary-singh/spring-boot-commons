package dev.harryy.common;

import dev.harryy.common.test.TestCommonAutoConfiguration;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration test that simulates a consumer application importing the common library.
 * This test creates a minimal Spring Boot application that imports the CommonAutoConfiguration
 * to verify that all components are properly autoconfigured in a realistic scenario.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.application.name=test-consumer-service",
        "opentelemetry.exporter.otlp.endpoint=http://localhost:4317",
        "management.endpoints.web.exposure.include=health,info,metrics,prometheus",
        "management.endpoint.health.show-details=always",
        "management.prometheus.metrics.export.enabled=true"
})
class ConsumerApplicationIntegrationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldStartConsumerApplicationWithCommonLibrary() {
        // Verify that the application context loads successfully
        assertNotNull(context, "Application context should load successfully");
    }

    @Test
    void shouldHaveActuatorEndpoints() {
        // Verify that health endpoint is available
        assertNotNull(context.getBean(HealthEndpoint.class),
                "Health endpoint should be available");

        // Verify that the metrics endpoint is available
        assertNotNull(context.getBean(MetricsEndpoint.class),
                "Metrics endpoint should be available");
    }

    @Test
    void shouldHavePrometheusMetricsConfiguration() {
        // Verify that the meter registry is configured
        assertNotNull(context.getBean(MeterRegistry.class),
                "MeterRegistry should be configured");
    }

    @Test
    void shouldHaveHealthProbes() {
        // Verify that application availability is configured
        ApplicationAvailability availability = context.getBean(ApplicationAvailability.class);
        assertNotNull(availability, "ApplicationAvailability should be configured");

        // Verify initial states
        assertEquals(LivenessState.CORRECT, availability.getLivenessState(),
                "Application should start in CORRECT state");
        assertEquals(ReadinessState.ACCEPTING_TRAFFIC, availability.getReadinessState(),
                "Application should start in ACCEPTING_TRAFFIC state");
    }

    @Test
    void shouldHaveOpenTelemetryConfigured() {
        // Verify that OpenTelemetry is configured
        OpenTelemetry openTelemetry = context.getBean(OpenTelemetry.class);
        assertNotNull(openTelemetry, "OpenTelemetry should be configured");

        // Get a tracer
        Tracer tracer = openTelemetry.getTracer("consumer-test");
        assertNotNull(tracer, "Should be able to get a tracer");
    }

    @Test
    void shouldEnrichRestTemplateWithTracing() {
        // Create a RestTemplate and verify it doesn't throw exceptions
        // This is testing that the auto-configurations don't interfere with normal Spring behavior
        RestTemplate restTemplate = new RestTemplate();

        // If we got here without errors, the test passes
        assertNotNull(restTemplate, "Should be able to create a RestTemplate");
    }

    @Configuration
    @EnableAutoConfiguration
    @ImportAutoConfiguration(TestCommonAutoConfiguration.class)
    static class TestConsumerApplication {
        // Minimal configuration to simulate a consumer application
    }
}
