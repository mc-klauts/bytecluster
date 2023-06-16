package net.bytemc.bytecluster.wrapper.dependency;

import net.bytemc.cluster.api.dependency.Dependency;
import net.bytemc.cluster.api.dependency.DependencyHandler;

import java.nio.file.Path;
import java.util.List;

public final class WrapperDependencyHandler implements DependencyHandler {

    @Override
    public Path getLibrariesDirectory() {
        //todo
        return null;
    }

    @Override
    public void registerDependency(Dependency dependency) {
        //todo
    }

    @Override
    public List<Dependency> getDependencies() {
        //todo
        return null;
    }
}
