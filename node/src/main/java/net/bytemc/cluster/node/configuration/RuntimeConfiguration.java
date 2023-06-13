package net.bytemc.cluster.node.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
public final class RuntimeConfiguration {

    public static final RuntimeConfiguration DEFAULT_CONFIGURATION = new RuntimeConfiguration(8879,
            new NodePath(
                    Path.of("storage"),
                    Path.of("temp"),
                    Path.of("templates")));

    private int port;
    private NodePath nodePath;

}
