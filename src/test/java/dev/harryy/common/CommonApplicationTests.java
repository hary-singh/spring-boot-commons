package dev.harryy.common;

import dev.harryy.common.test.TestCommonAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {TestCommonAutoConfiguration.class})
class CommonApplicationTests {

    @Test
    void contextLoads() {
        // Basic test to verify the Spring context loads successfully
        Assertions.assertDoesNotThrow(() -> {
            // No action needed, just checking if the context loads
        });
    }

}
