package net.bytemc.cluster.node.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigurationHelper {

    public static void createDirectoryIfNotExists(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteIfNotExists(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createConfigurationIfNotExists(Path path, Object value) {
        if (!Files.exists(path)) {
            ConfigurationProvider.write(path, value);
        }
    }

    public static <T> T readConfiguration(Path path, T value) {
        createConfigurationIfNotExists(path, value);
        return (T) ConfigurationProvider.read(path, value.getClass());
    }
}
