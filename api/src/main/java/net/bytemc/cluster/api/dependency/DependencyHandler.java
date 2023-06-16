package net.bytemc.cluster.api.dependency;

import java.nio.file.Path;
import java.util.List;

public interface DependencyHandler {

    String DEFAULT_MAVEN_REPO = "https://repo1.maven.org/maven2/";

    Path getLibrariesDirectory();

    void registerDependency(Dependency dependency);

    List<Dependency> getDependencies();

}
