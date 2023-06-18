package net.bytemc.cluster.node.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.node.modules.content.ModuleContentInfo;
import net.bytemc.cluster.node.modules.loader.ModuleClassLoader;

import java.io.File;

@Getter
@AllArgsConstructor
public class LoadedModule {

    private CloudModule module;
    private File file;
    private ModuleContentInfo info;
    private ModuleClassLoader classLoader;

}
