package dev.harryy.common.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * Configures metrics for Prometheus scraping via Spring Boot Actuator.
 * Prometheus uses a pull model where it periodically scrapes the /actuator/prometheus endpoint.
 */
@AutoConfiguration(after = {MetricsAutoConfiguration.class, PrometheusMetricsExportAutoConfiguration.class})
@ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
@PropertySource("classpath:prometheus.properties")
public class PrometheusMetricsConfig {

    private static final Logger logger = LoggerFactory.getLogger(PrometheusMetricsConfig.class);

    @Value("${spring.application.name:unknown-service}")
    private String applicationName;

    /**
     * Configures common metrics that should be exposed for all services.
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> configureMeterRegistry() {
        return meterRegistry -> {
            // Tag all metrics with the application name
            meterRegistry.config().commonTags("application", applicationName);

            // Register JVM and system metrics binders
            new ClassLoaderMetrics().bindTo(meterRegistry);
            new JvmMemoryMetrics().bindTo(meterRegistry);

            try (JvmGcMetrics jvmGcMetrics = new JvmGcMetrics()) {
                jvmGcMetrics.bindTo(meterRegistry);
                logger.debug("JVM GC metrics registered");
            }

            new JvmThreadMetrics().bindTo(meterRegistry);
            new ProcessorMetrics().bindTo(meterRegistry);
            new UptimeMetrics().bindTo(meterRegistry);

            logger.info("Configured metric registry with application name: {}", applicationName);
        };
    }
}
