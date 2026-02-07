package dev.rebelcraft.scarlet.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test to verify ScarletProperties is properly wired into Spring context.
 */
@SpringBootTest
class ScarletPropertiesIntegrationTest {

    @Autowired
    private ScarletProperties scarletProperties;

    @Test
    void shouldInjectScarletPropertiesBean() {
        assertNotNull(scarletProperties, "ScarletProperties bean should be injected");
    }

    @Test
    void shouldHaveWorkingDirectory() {
        Path workingDir = scarletProperties.workingDir();
        assertNotNull(workingDir, "Working directory should not be null");
        assertEquals(Paths.get(".scarlet"), workingDir, "Working directory should match configured value");
    }
}
