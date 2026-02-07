package dev.rebelcraft.scarlet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.nio.file.Path;

/**
 * Configuration properties for the Scarlet application.
 */
@ConfigurationProperties(prefix = "scarlet.app")
public record ScarletProperties(
    @DefaultValue(".scarlet") Path workingDir
) {
}
