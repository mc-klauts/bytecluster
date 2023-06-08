package net.bytemc.cluster.api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.nio.file.Path;

@Getter
@AllArgsConstructor
public enum CloudGroupType {

    VELOCITY("velocity.jar", true),
    MINESTOM("minestom.jar");

    String fileId;
    boolean proxy = false;

    CloudGroupType(String fileId) {
        this.fileId = fileId;
    }

    public Path getPath(Path directory) {
        return directory.resolve(fileId);
    }
}
