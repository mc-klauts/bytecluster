package net.bytemc.cluster.node.dependency;

import lombok.Getter;
import net.bytemc.cluster.api.dependency.Dependency;
import net.bytemc.cluster.api.dependency.DependencyHandler;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.misc.FileHelper;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DependencyHandlerImpl implements DependencyHandler {

    @Getter
    private List<Dependency> dependencies = new ArrayList<>();

    public DependencyHandlerImpl() {
        FileHelper.createDirectoryIfNotExists(getLibrariesDirectory());

        // default

        this.registerDependency(new Dependency("JLINE", "org.jline", "jline", "3.23.0", DEFAULT_MAVEN_REPO));
        this.registerDependency(new Dependency("JANSI", "org.fusesource.jansi", "jansi", "2.4.0", DEFAULT_MAVEN_REPO));
    }

    @Override
    public Path getLibrariesDirectory() {
        return Node.getInstance().getRuntimeConfiguration().getNodePath().getDependencyPath();
    }

    @Override
    public void registerDependency(Dependency dependency) {
        dependency.download(this);
        try {
            final var method = this.getClass().getClassLoader().getClass().getMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(this.getClass().getClassLoader(), dependency.getFile(this).toURI().toURL());
        } catch (NoSuchMethodException | MalformedURLException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        this.dependencies.add(dependency);
    }
}
