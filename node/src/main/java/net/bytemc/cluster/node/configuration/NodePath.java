package net.bytemc.cluster.node.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
public final class NodePath {

    private Path storagePath;
    private Path serverRunningPath;
    private Path templatePath;
    private Path dependencyPath;

}
