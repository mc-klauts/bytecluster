package net.bytemc.cluster.node.misc;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;

public final class FileHelper {

    public static void createDirectoryIfNotExists(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectory(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteIfExists(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDirectory(final @NotNull Path directory) {
        try (final var pathStream = Files.walk(directory)) {
            pathStream.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException ignore) {}
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replaceLine(final Path file, final Function<String, String> replace) {
        final var lines = new ArrayList<String>();

        try (final var bufferedReader = Files.newBufferedReader(file)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (final var writer = Files.newBufferedWriter(file)) {
            for (final var line : lines) {
                writer.write(replace.apply(line) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyDirectory(final @NotNull Path from, final @NotNull Path to) {
        try (final var pathStream = Files.walk(from)) {
            pathStream.forEach(path -> {
                final var resolvedTo = to.resolve(from.relativize(path));

                try {
                    if (Files.isDirectory(path)) {
                        if (!Files.exists(resolvedTo)) {
                            Files.createDirectory(resolvedTo);
                        }
                    } else {
                        Files.copy(path, resolvedTo, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
