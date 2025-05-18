package dev.harryy.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configures standardized logging across microservices.
 * Uses Logback with JSON formatting via logstash-logback-encoder for easy ingestion into Loki/Grafana.
 */
@Configuration
@PropertySource("classpath:logging.properties")
public class LoggingConfig {
    // Configuration is handled via logback-spring.xml included in resources
}
