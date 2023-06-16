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

        this.registerDependency(new Dependency("GSON", "com.google.code.gson", "gson", "2.10.1", DEFAULT_MAVEN_REPO));


        this.registerDependency(new Dependency("JLINE", "org.jline", "jline", "3.23.0", DEFAULT_MAVEN_REPO));
        this.registerDependency(new Dependency("JANSI", "org.fusesource.jansi", "jansi", "2.4.0", DEFAULT_MAVEN_REPO));

        // netty
        this.registerDependency(new Dependency("NETTY_COMMON", "io.netty", "netty5-common", "5.0.0.Alpha5", DEFAULT_MAVEN_REPO));
        this.registerDependency(new Dependency("NETTY_CODEC", "io.netty", "netty5-codec", "5.0.0.Alpha5", DEFAULT_MAVEN_REPO));
        this.registerDependency(new Dependency("NETTY_BUFFER", "io.netty", "netty5-buffer", "5.0.0.Alpha5", DEFAULT_MAVEN_REPO));
        this.registerDependency(new Dependency("NETTY_RESOLVER", "io.netty", "netty5-resolver", "5.0.0.Alpha5", DEFAULT_MAVEN_REPO));
        this.registerDependency(new Dependency("NETTY_TRANSPORT", "io.netty", "netty5-transport", "5.0.0.Alpha5", DEFAULT_MAVEN_REPO));
        //netty epoll
        this.registerDependency(new Dependency("NETTY_TRANSPORT_CLASSES_EPOLL", "io.netty", "netty5-transport-classes-epoll", "5.0.0.Alpha5", DEFAULT_MAVEN_REPO));
        this.registerDependency(new Dependency("NETTY_TRANSPORT_NATIVE_EPOLL_LINUX_x86_64", "io.netty", "netty5-transport-native-epoll", "5.0.0.Alpha5", DEFAULT_MAVEN_REPO, "linux-x86_64"));
        this.registerDependency(new Dependency("NETTY_TRANSPORT_NATIVE_EPOLL_LINUX_AARCH_64", "io.netty", "netty5-transport-native-epoll", "5.0.0.Alpha5", DEFAULT_MAVEN_REPO, "linux-aarch_64"));
        this.registerDependency(new Dependency("NETTY_TRANSPORT_NATIVE_UNIX_COMMON", "io.netty", "netty5-transport-native-unix-common", "5.0.0.Alpha5", DEFAULT_MAVEN_REPO));
    }

    @Override
    public Path getLibrariesDirectory() {
        return Path.of("dependencies");
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
