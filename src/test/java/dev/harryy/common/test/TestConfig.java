package dev.harryy.common.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Test configuration that enables Spring Boot's autoconfiguration.
 * This allows tests to verify that our library's auto-configurations
 * are correctly loaded and applied.
 */
@Configuration
@EnableAutoConfiguration
public class TestConfig {
}
