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
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
    mavenCentral()
}
```

### Add Dependency

```groovy
dependencies {
    implementation 'dev.harryy:spring-boot-commons:0.0.1-SNAPSHOT'
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

### Compatibility

- Spring Boot 3.4+
- Java 21+