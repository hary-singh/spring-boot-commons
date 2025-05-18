package dev.harryy.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Main autoconfiguration class that imports all common configurations.
 * When a microservice includes this common library, these configurations will be automatically applied.
 */
@Configuration
@Import({
        ActuatorConfig.class,
        OpenTelemetryConfig.class,
        LoggingConfig.class,
        HealthProbesConfig.class,
        PrometheusMetricsConfig.class
})
public class CommonAutoConfiguration {
}
