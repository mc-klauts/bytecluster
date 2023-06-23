package net.bytemc.cluster.node.configuration;

import net.bytemc.cluster.api.misc.GsonHelper;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigurationHelper {

    public static void createConfigurationIfNotExists(Path path, Object value) {
        if (!Files.exists(path)) {
            GsonHelper.write(path, value);
        }
    }

    public static <T> T readConfiguration(Path path, T value) {
        createConfigurationIfNotExists(path, value);
        return (T) GsonHelper.read(path, value.getClass());
    }
}
