# Spring Boot Commons

A comprehensive library providing standardized configurations for Spring Boot microservices with a focus on
observability and operational excellence.

## Features

- **Complete Observability Stack**
    - Distributed tracing with OpenTelemetry
    - Prometheus metrics collection
    - Structured JSON logging with trace context correlation
    - Health probes for Kubernetes environments

- **Zero-Configuration Setup**
    - Autoconfigures when added as a dependency
    - Sensible defaults with override capability

- **Production-Ready Components**
    - Kubernetes-compatible health probes
    - Prometheus metric collection for dashboards
    - Distributed tracing for request flows
    - Standardized logging format

## Installation

### Add GitHub Packages Repository

```groovy
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/hary-singh/spring-boot-commons")
    }
    mavenCentral()
}
```

### Add Dependency

```groovy
dependencies {
  implementation 'dev.harryy:spring-boot-commons:0.0.1'
}
```

## Usage

The library autoconfigures when added as a dependency. No additional setup is required for basic functionality.

### Access Points

- **Prometheus Metrics**: Access via `/actuator/prometheus`
- **Health Probes**: Access via `/actuator/health`
- **Distributed Tracing**: Integrated with OpenTelemetry

### Configuration Properties

Override default configurations in `application.properties` or `application.yml`.

```yaml
# OpenTelemetry endpoint for your environment
opentelemetry.exporter.otlp.endpoint=http://tempo:4317

        # Disable specific features if needed
opentelemetry.enabled=true
management.prometheus.metrics.export.enabled=true
```

### Manual Health State Control

For advanced scenarios, inject the health controller:

```java
@Autowired
private HealthProbesConfig.HealthStateController healthController;

healthController.markNotReady(); // Temporarily refuse traffic

healthController.markReady();    // Accept traffic again
```

## Configuration Details

### OpenTelemetry Configuration

This library provides pre-configured OpenTelemetry tracing with sensible defaults. The following properties can be
customized:

```properties
# Main configuration
opentelemetry.enabled=true
opentelemetry.service.name=your-service-name
opentelemetry.exporter.otlp.endpoint=http://your-collector:4317

# Advanced settings
opentelemetry.sampler.probability=1.0
opentelemetry.propagators=tracecontext,baggage
```

### Logging Configuration

Structured JSON logging is provided with MDC context propagation for distributed tracing:

```properties
# Optional: Override log pattern or other logback settings
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

For major changes, please open an issue first to discuss what you would like to change.

## Security

Please see the [Security Policy](SECURITY.md) for details on reporting vulnerabilities.

## Compatibility

- Spring Boot 3.4+
- Java 21+

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

