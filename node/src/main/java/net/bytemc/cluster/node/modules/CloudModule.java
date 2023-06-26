package net.bytemc.cluster.node.modules;

import net.bytemc.cluster.api.misc.FileHelper;
import net.bytemc.cluster.node.Node;
import java.nio.file.Path;

public interface CloudModule {

    void onEnable();

    void onDisable();

    default Path getConfigurationPath() {
        var info = Node.getInstance().getModuleHandler().getLoadedModules().stream().filter(it -> it.getModule().equals(this)).findFirst().get().getInfo();
        var path = CloudModuleHandler.MODULE_PATH.resolve(info.getName());

        //check if configuration path is exists
        FileHelper.createDirectoryIfNotExists(path);

        return Path.of(info.getName());
    }
}
