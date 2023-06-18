package net.bytemc.cluster.node.modules.content;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ModuleContentInfo {

    private final String name;
    private final String author;
    private final String mainClass;
    private final ModuleCopyType moduleCopyType;

}
