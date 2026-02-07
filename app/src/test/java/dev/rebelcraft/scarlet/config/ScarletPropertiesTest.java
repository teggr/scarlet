package dev.rebelcraft.scarlet.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ScarletProperties configuration.
 */
@SpringBootTest
class ScarletPropertiesTest {

    @Autowired
    private ScarletProperties scarletProperties;

    @Test
    void shouldLoadDefaultWorkingDirectory() {
        assertNotNull(scarletProperties);
        assertNotNull(scarletProperties.workingDir());
        assertEquals(Paths.get(".scarlet"), scarletProperties.workingDir());
    }
}
