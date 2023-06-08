package net.bytemc.cluster.node.misc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
}
