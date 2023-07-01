package net.bytemc.cluster.api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.nio.file.Path;

@Getter
@AllArgsConstructor
public enum CloudGroupType {

    VELOCITY("velocity.jar", true),
    MINESTOM("minestom.jar"),

    PAPER_1_20_1("paper-1.20.1.jar");

    String fileId;
    boolean proxy = false;

    CloudGroupType(String fileId) {
        this.fileId = fileId;
    }

    public Path getPath(Path directory) {
        return directory.resolve(fileId);
    }
}
