package net.bytemc.cluster.node.modules;

import net.bytemc.cluster.api.misc.FileHelper;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.configuration.ConfigurationHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface CloudModule {

    void onEnable();

    void onDisable();

    default Path getConfigurationPath() {
        var info = Node.getInstance().getModuleHandler().getLoadedModules().stream().filter(it -> it.getModule().equals(this)).findFirst().get().getInfo();
        var path = CloudModuleHandler.MODULE_PATH.resolve(info.getName());

        //check if configuration path is exists
        FileHelper.createDirectoryIfNotExists(path);

        return path;
    }

    default <T> T setConfiguration(Path path, T value) {
        return ConfigurationHelper.readConfiguration(getConfigurationPath().resolve(path), value);
    }

    default void removeConfiguration(Path path) {
        try {
            Files.deleteIfExists(getConfigurationPath().resolve(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void updateConfiguration(Path path, Object value) {
        this.removeConfiguration(path);
        this.setConfiguration(path, value);
    }
}
