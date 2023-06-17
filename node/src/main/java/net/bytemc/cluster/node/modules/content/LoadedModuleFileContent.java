package net.bytemc.cluster.node.modules.content;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@Getter
@AllArgsConstructor
public class LoadedModuleFileContent {

    private File file;
    private ModuleContentInfo info;

}
