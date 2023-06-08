package net.bytemc.cluster.node.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
public final class RuntimeConfiguraiton {

    public static RuntimeConfiguraiton DEFAULT_CONFIGURATION = new RuntimeConfiguraiton(8879, new NodePath(Path.of("storage")));

    private int port;
    private NodePath nodePath;

}
