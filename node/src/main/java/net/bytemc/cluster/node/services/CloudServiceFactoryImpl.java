package net.bytemc.cluster.node.services;

import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceFactory;
import net.bytemc.cluster.api.service.CloudServiceState;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.api.misc.FileHelper;
import net.bytemc.cluster.node.misc.VelocityForwardingSecretHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public final class CloudServiceFactoryImpl implements CloudServiceFactory {

    private static final Path STORAGE_PATH = Path.of("storage");
    private static final List<String> VELOCITY_FLAGS = Arrays.asList(
            "-XX:+UseG1GC",
            "-XX:G1HeapRegionSize=4M",
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:+ParallelRefProcEnabled",
            "-XX:+AlwaysPreTouch",
            "-XX:MaxInlineLevel=15"
    );

    private static final String WRAPPER_MAIN_CLASS;

    static {
        try {
            WRAPPER_MAIN_CLASS = new JarInputStream(Files.newInputStream(STORAGE_PATH.resolve("bytecluster-wrapper.jar")))
                    .getManifest().getMainAttributes().getValue("Main-Class");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CloudServiceFactoryImpl() {
        var runningPath = CloudServiceFactoryQueue.TEMP_PATH;

        if (Files.exists(runningPath)) {
            FileHelper.deleteDirectory(runningPath);
            Logger.info("Clean up on running service directory...");
        }
        FileHelper.createDirectoryIfNotExists(CloudServiceFactoryQueue.TEMP_PATH);
    }

    @Override
    public void start(@NotNull CloudService service) {

        if (service instanceof LocalCloudService cloudService) {

            // copy template before copy runtime files
            var serviceDirectory = cloudService.getDirectory();
            if ((!Files.exists(serviceDirectory) && service.getGroup().isStaticService()) || !service.getGroup().isStaticService()) {

                FileHelper.createDirectoryIfNotExists(serviceDirectory);

                Node.getInstance().getTemplateHandler().copyTemplate("GLOBAL", cloudService);
                Node.getInstance().getTemplateHandler().copyTemplate("EVERY_" + (cloudService.getGroup().getGroupType().isProxy() ? "PROXY" : "SERVER"), cloudService);
                Node.getInstance().getTemplateHandler().copyTemplate(cloudService.getGroupName(), cloudService);
            }

            try {
                Files.copy(service.getGroup().getGroupType().getPath(STORAGE_PATH), service.getGroup().getGroupType().getPath(((LocalCloudService) service).getDirectory()), StandardCopyOption.REPLACE_EXISTING);

                if (cloudService.getGroup().getGroupType() == CloudGroupType.MINESTOM) {
                    FileHelper.createDirectoryIfNotExists(serviceDirectory.resolve("extensions"));
                    Files.copy(STORAGE_PATH.resolve("bytecluster-plugin.jar"), serviceDirectory.resolve("extensions").resolve("bytecluster-plugin.jar"), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    FileHelper.createDirectoryIfNotExists(serviceDirectory.resolve("plugins"));
                    Files.copy(STORAGE_PATH.resolve("bytecluster-plugin.jar"), serviceDirectory.resolve("plugins").resolve("bytecluster-plugin.jar"), StandardCopyOption.REPLACE_EXISTING);
                    ;
                }

                Node.getInstance().getModuleHandler().copyModuleFiles(cloudService);

            } catch (IOException e) {
                Logger.error("Cannot copy service runtime file. Service is now closed.", e);
                service.shutdown();
                return;
            }

            if (cloudService.getGroup().getGroupType() == CloudGroupType.VELOCITY) {
                // copy important service files
                // velocity.toml
                final var velocityFile = serviceDirectory.resolve("velocity.toml");
                if (Files.notExists(velocityFile)) {
                    try (final var inputStream = this.getClass().getClassLoader().getResourceAsStream("velocity.toml")) {
                        assert inputStream != null;
                        Files.copy(inputStream, velocityFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // change port in velocity configuration
                FileHelper.replaceLine(velocityFile, line -> line.startsWith("bind = ") ? "bind = \"0.0.0.0:" + service.getPort() + "\"" : line);
                VelocityForwardingSecretHelper.generate(serviceDirectory);
            }

            Logger.info("Told local node to start service " + cloudService.getName());

            try {
                var process = new ProcessBuilder(arguments(cloudService))
                        .redirectError(serviceDirectory.resolve("wrapper.error").toFile())
                        .redirectOutput(serviceDirectory.resolve("wrapper.output").toFile())
                        .directory(serviceDirectory.toFile())
                        .start();
                cloudService.setProcess(process);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void stop(CloudService service) {
        if (service instanceof LocalCloudService localService) {
            localService.setState(CloudServiceState.STOPPED);
            synchronized (this) {
                if (localService.getProcess() != null && localService.getProcess().toHandle().isAlive()) {

                    if (service.getGroup().getGroupType() == CloudGroupType.MINESTOM) {
                        service.executeCommand("close");
                    } else {
                        service.executeCommand("stop");
                    }

                    try {
                        if (localService.getProcess().waitFor(5, TimeUnit.SECONDS)) {
                            localService.getProcess().exitValue();
                            localService.setProcess(null);


                            if (!localService.getGroup().isStaticService()) {
                                FileHelper.deleteDirectory(localService.getDirectory());
                            }
                            Logger.info("Service " + localService.getName() + " is now stopped.");
                            return;
                        }
                    } catch (InterruptedException ignored) {
                    }
                    localService.getProcess().toHandle().destroyForcibly();
                    localService.setProcess(null);
                    if (!localService.getGroup().isStaticService()) {
                        FileHelper.deleteDirectory(localService.getDirectory());
                    }
                    Logger.info("Service " + localService.getName() + " is now stopped.");
                }
            }
        }
    }

    private @NotNull List<String> arguments(@NotNull LocalCloudService service) {
        final var wrapper = STORAGE_PATH.resolve("bytecluster-wrapper.jar").toAbsolutePath();
        final var group = service.getGroup();
        final var arguments = new ArrayList<String>();
        arguments.add("java");
        if (group.getGroupType() == CloudGroupType.VELOCITY) {
            arguments.addAll(VELOCITY_FLAGS);
        }
        arguments.add("-Xms" + group.getMaxMemory() + "M");
        arguments.add("-Xmx" + group.getMaxMemory() + "M");
        arguments.add("-cp");
        arguments.add(wrapper.toString());
        arguments.add("-javaagent:" + wrapper);
        arguments.add(WRAPPER_MAIN_CLASS);

        final var applicationFile = group.getGroupType().getPath(service.getDirectory());

        var preLoadClasses = false;
        try (final var jarFile = new JarFile(applicationFile.toFile())) {
            arguments.add(jarFile.getManifest().getMainAttributes().getValue("Main-Class"));
            preLoadClasses = jarFile.getEntry("META-INF/versions.list") != null;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        arguments.add(applicationFile.toAbsolutePath().toString());
        arguments.add(Boolean.toString(preLoadClasses));
        arguments.add(service.getName());
        arguments.add("127.0.0.1");
        arguments.add(String.valueOf(Node.getInstance().getRuntimeConfiguration().getPort()));
        arguments.add(String.valueOf(service.getPort()));

        if (service.getGroup().getGroupType() == CloudGroupType.MINESTOM) {
            arguments.add(VelocityForwardingSecretHelper.TOKEN);
        }
        return arguments;
    }
}
