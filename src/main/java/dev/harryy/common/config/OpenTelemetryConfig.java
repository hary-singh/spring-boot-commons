package dev.harryy.common.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Configures OpenTelemetry for distributed tracing.
 * This allows microservices to automatically trace requests without manual code changes.
 */
@Configuration
@PropertySource("classpath:opentelemetry.properties")
public class OpenTelemetryConfig {

    private final Environment environment;

    @Value("${opentelemetry.exporter.otlp.endpoint:http://localhost:4317}")
    private String otlpEndpoint;
    @Value("${spring.application.name:unknown-service}")
    private String applicationName;

    public OpenTelemetryConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @ConditionalOnProperty(value = "opentelemetry.enabled", havingValue = "true", matchIfMissing = true)
    public OpenTelemetry openTelemetry() {
        // Get service name from OTEL_SERVICE_NAME env var first, then from spring.application.name
        String serviceName = getServiceName();

        Resource resource = Resource.getDefault()
                .merge(Resource.create(Attributes.builder()
                        .put("service.name", serviceName)
                        .build()));

        // Create the OTLP exporter with a simpler approach to avoid version compatibility issues
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(otlpEndpoint)
                .build();

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .build();

        return OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();
    }

    /**
     * Gets the service name with the following priority:
     * 1. OTEL_SERVICE_NAME environment variable
     * 2. spring.application.name property
     * 3. Fallback to "unknown-service"
     */
    private String getServiceName() {
        // Check environment variable first (standard OpenTelemetry approach)
        String otelServiceName = environment.getProperty("OTEL_SERVICE_NAME");
        if (otelServiceName != null && !otelServiceName.isBlank()) {
            return otelServiceName;
        }

        // Fall back to spring.application.name
        return applicationName;
    }
}
