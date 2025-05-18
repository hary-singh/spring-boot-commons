package dev.harryy.common.test;

import dev.harryy.common.config.ActuatorConfig;
import dev.harryy.common.config.HealthProbesConfig;
import dev.harryy.common.config.LoggingConfig;
import dev.harryy.common.config.PrometheusMetricsConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Test-specific version of CommonAutoConfiguration that uses
 * the test OpenTelemetry configuration instead of the real one.
 */
@Configuration
@Import({
        ActuatorConfig.class,
        LoggingConfig.class,
        HealthProbesConfig.class,
        PrometheusMetricsConfig.class
})
public class TestCommonAutoConfiguration {
}
