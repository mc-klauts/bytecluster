package net.bytemc.cluster.launcher;

import net.bytemc.cluster.launcher.url.BoostrapUrlLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public final class ClusterLauncher {

    public ClusterLauncher(String[] args) {

        try {

            final var path = Path.of("storage");

            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            var classLoader = ClassLoader.getSystemClassLoader();
            Files.copy(Objects.requireNonNull(classLoader.getResourceAsStream("bytecluster-node.jar")), path.resolve("bytecluster-node.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(classLoader.getResourceAsStream("bytecluster-wrapper.jar")), path.resolve("bytecluster-wrapper.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(classLoader.getResourceAsStream("bytecluster-plugin.jar")), path.resolve("bytecluster-plugin.jar"), StandardCopyOption.REPLACE_EXISTING);


            BoostrapUrlLoader loader = new BoostrapUrlLoader(new URL[]{path.resolve("bytecluster-node.jar").toUri().toURL()}, classLoader);

            Thread.currentThread().setContextClassLoader(loader);
            loader.loadClass("net.bytemc.cluster.node.NodeLauncher").getMethod("main", String[].class).invoke(null, (Object) args);

        } catch (IOException | ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
