package dev.harryy.common.config;

import dev.harryy.common.test.TestCommonAutoConfiguration;
import dev.harryy.common.test.TestConfig;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests to verify that the CommonAutoConfiguration properly imports and configures all components.
 * This is an integration test that ensures all configurations work together correctly.
 */
@SpringBootTest(classes = {TestConfig.class, TestCommonAutoConfiguration.class})
@TestPropertySource(properties = {
        "spring.application.name=common-auto-test-service",
        "management.endpoints.web.exposure.include=health,info,metrics,prometheus",
        "management.endpoint.health.show-details=always",
        "management.prometheus.metrics.export.enabled=true"
})
class CommonAutoConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldRegisterAllComponents() {
        // Verify that ActuatorConfig components are registered
        assertNotNull(context.getBean(HealthEndpoint.class),
                "Health endpoint should be registered");

        // Verify that HealthProbesConfig components are registered
        assertNotNull(context.getBean(ApplicationAvailability.class),
                "ApplicationAvailability should be registered");
        assertNotNull(context.getBean(HealthProbesConfig.HealthStateController.class),
                "HealthStateController should be registered");

        // Verify that OpenTelemetry components are registered
        assertNotNull(context.getBean(OpenTelemetry.class),
                "OpenTelemetry should be registered");

        // Verify that PrometheusMetricsConfig components are registered
        assertNotNull(context.getBean(MeterRegistry.class),
                "MeterRegistry should be registered");
    }
}
