package net.bytemc.cluster.node.dependency;

import lombok.Getter;
import net.bytemc.cluster.api.misc.FileHelper;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDependencyHandler implements DependencyHandler {

    public static String DEFAULT_MAVEN_REPO = "https://repo1.maven.org/maven2/";

    @Getter
    private List<Dependency> dependencies = new ArrayList<>();

    public AbstractDependencyHandler() {
        FileHelper.createDirectoryIfNotExists(getLibrariesDirectory());
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
