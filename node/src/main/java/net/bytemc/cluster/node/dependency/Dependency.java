package net.bytemc.cluster.node.dependency;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

@Getter
@AllArgsConstructor
public final class Dependency {

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    private final String name;
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String repository;
    private String classifier;

    public Dependency(String name, String groupId, String artifactId, String version, String repository) {
        this.name = name;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repository = repository;
    }

    public void download(DependencyHandler dependencyHandler) {
        if(isPresent(dependencyHandler)) return;
        try {
            final var urlConnection = new URL(repository + getMavenRepoPath()).openConnection();
            try (final var inputStream = urlConnection.getInputStream()) {
                Files.write(dependencyHandler.getLibrariesDirectory().resolve(getFileName()), inputStream.readAllBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMavenRepoPath() {
        return String.format(MAVEN_FORMAT, groupId.replace('.', '/'), artifactId, version, artifactId, version + (classifier != null ? "-" + classifier : ""));
    }

    public boolean isPresent(DependencyHandler dependencyHandler) {
        return Files.exists(dependencyHandler.getLibrariesDirectory().resolve(getFileName()));
    }

    public String getFileName() {
        return this.name.toLowerCase().replace('_', '-') + "-" + this.version + ".jar";
    }

    public File getFile(DependencyHandler dependencyHandler) {
        return dependencyHandler.getLibrariesDirectory().resolve(getFileName()).toFile();
    }
}