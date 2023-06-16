package net.bytemc.cluster.node.dependency;

import java.nio.file.Path;
import java.util.List;

public interface DependencyHandler {

    Path getLibrariesDirectory();

    void registerDependency(Dependency dependency);

    List<Dependency> getDependencies();

    default ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

}
