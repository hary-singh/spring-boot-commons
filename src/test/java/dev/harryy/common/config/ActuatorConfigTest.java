package dev.harryy.common.config;

import dev.harryy.common.test.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests to verify that the ActuatorConfig properly sets up Spring Boot Actuator endpoints.
 */
@SpringBootTest(classes = {TestConfig.class, ActuatorConfig.class, HealthContributorAutoConfiguration.class})
@TestPropertySource(properties = {
        "spring.application.name=actuator-test-service",
        "management.endpoints.web.exposure.include=health,info,metrics,prometheus",
        "management.endpoint.health.show-details=always"
})
class ActuatorConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldRegisterActuatorEndpoints() {
        // Verify that critical health endpoints are registered
        assertNotNull(context.getBean(HealthEndpoint.class), "Health endpoint should be registered");

        // Verify that info endpoint is registered
        assertNotNull(context.getBean(InfoEndpoint.class), "Info endpoint should be registered");

        // Verify that the metrics endpoint is registered
        assertNotNull(context.getBean(MetricsEndpoint.class), "Metrics endpoint should be registered");
    }

    @Test
    void shouldLoadPropertyValues() {
        // Verify that properties from actuator.properties are loaded
        String exposedEndpoints = context.getEnvironment()
                .getProperty("management.endpoints.web.exposure.include");

        assertNotNull(exposedEndpoints, "Exposed endpoints property should be loaded");
        assertTrue(exposedEndpoints.contains("health"), "Health endpoint should be exposed");
        assertTrue(exposedEndpoints.contains("prometheus"), "Prometheus endpoint should be exposed");
    }
}
