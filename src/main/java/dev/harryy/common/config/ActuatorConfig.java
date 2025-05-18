package dev.harryy.common.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.info.InfoEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.PropertySource;

/**
 * Configures Spring Boot Actuator with sensible defaults.
 * Exposes health, info, metrics, and prometheus endpoints.
 */
@AutoConfiguration(
        after = {
                EndpointAutoConfiguration.class,
                WebEndpointAutoConfiguration.class,
                HealthEndpointAutoConfiguration.class,
                InfoEndpointAutoConfiguration.class,
                MetricsAutoConfiguration.class,
                PrometheusMetricsExportAutoConfiguration.class
        }
)
@ConditionalOnClass(name = "org.springframework.boot.actuate.endpoint.annotation.Endpoint")
@PropertySource("classpath:actuator.properties")
public class ActuatorConfig {

    private static final Logger logger = LoggerFactory.getLogger(ActuatorConfig.class);

    @PostConstruct
    public void logActuatorInitialization() {
        logger.info("Initialized actuator endpoints from common library");
    }
}

