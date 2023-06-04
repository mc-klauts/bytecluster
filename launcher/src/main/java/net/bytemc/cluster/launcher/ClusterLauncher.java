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

    public ClusterLauncher(String args[]) {

        try {

            final var path = Path.of("storage", "node.jar");
            final var storage = path.getParent();

            if (!Files.exists(storage)) {
                Files.createDirectory(storage);
            }

            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("node.jar")), path, StandardCopyOption.REPLACE_EXISTING);
            BoostrapUrlLoader loader = new BoostrapUrlLoader(new URL[]{path.toUri().toURL()}, ClassLoader.getSystemClassLoader());

            Thread.currentThread().setContextClassLoader(loader);
            loader.loadClass("net.bytemc.cluster.node.NodeLauncher").getMethod("main", String[].class).invoke(null, (Object) args);

        } catch (IOException | ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
