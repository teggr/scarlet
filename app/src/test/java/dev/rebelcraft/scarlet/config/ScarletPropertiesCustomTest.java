package dev.rebelcraft.scarlet.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ScarletProperties configuration with custom values.
 */
@SpringBootTest
@TestPropertySource(properties = "scarlet.app.working-dir=/tmp/custom-scarlet")
class ScarletPropertiesCustomTest {

    @Autowired
    private ScarletProperties scarletProperties;

    @Test
    void shouldLoadCustomWorkingDirectory() {
        assertNotNull(scarletProperties);
        assertEquals(Paths.get("/tmp/custom-scarlet"), scarletProperties.workingDir());
    }
}
