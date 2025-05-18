package dev.harryy.common.config;

import dev.harryy.common.test.TestConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests to verify that the PrometheusMetricsConfig properly configures metrics for Prometheus.
 */
@SpringBootTest(classes = {TestConfig.class, PrometheusMetricsConfig.class,
        PrometheusMetricsConfigTest.TestMeterConfig.class, MetricsAutoConfiguration.class})
@TestPropertySource(properties = {
        "spring.application.name=metrics-test-service",
        "management.prometheus.metrics.export.enabled=true"
})
class PrometheusMetricsConfigTest {

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    void shouldConfigureMeterRegistry() {
        // Create at least one meter to check for tags
        meterRegistry.counter("test.counter").increment();

        // Get the counter and check its tags
        Counter counter = meterRegistry.find("test.counter").counter();
        assertNotNull(counter, "Counter should be registered");

        // Check if the application tag is added
        String applicationTag = counter.getId().getTag("application");
        assertEquals("metrics-test-service", applicationTag,
                "MeterRegistry should be tagged with application name");
    }

    @Test
    void shouldAddMetricBindersTags() {
        // Create a metric to verify tags
        meterRegistry.counter("test.counter.2").increment();

        // Verify that metric exists
        Counter counter = meterRegistry.find("test.counter.2").counter();
        assertNotNull(counter, "Test counter should be registered");

        // Verify that meter has application tag
        String applicationTag = counter.getId().getTag("application");
        assertEquals("metrics-test-service", applicationTag, "Counter should have application tag");
    }

    @TestConfiguration
    static class TestMeterConfig {
        @Bean
        public MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }
}
