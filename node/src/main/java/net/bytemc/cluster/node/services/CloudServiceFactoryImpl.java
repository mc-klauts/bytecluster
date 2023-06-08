package net.bytemc.cluster.node.services;

import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceFactory;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.logger.Logger;
import net.bytemc.cluster.node.misc.FileHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public final class CloudServiceFactoryImpl implements CloudServiceFactory {

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
            WRAPPER_MAIN_CLASS = new JarInputStream(Files.newInputStream(Node.getInstance().getRuntimeConfiguration().getNodePath().getStoragePath().resolve("wrapper.jar"))).getManifest().getMainAttributes().getValue("Main-Class");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CloudServiceFactoryImpl() {
        FileHelper.createDirectoryIfNotExists(Node.getInstance().getRuntimeConfiguration().getNodePath().getServerRunningPath());
    }

    @Override
    public void start(CloudService cloudServiceGroup) {

        FileHelper.createDirectoryIfNotExists(Node.getInstance().getRuntimeConfiguration().getNodePath().getServerRunningPath().resolve(cloudServiceGroup.getName()));


        if (cloudServiceGroup instanceof LocalCloudService cloudService) {
            Logger.info("Starting service " + cloudService.getName());

            /*
            try {
                var process = new ProcessBuilder(arguments(cloudService)).directory(cloudService.getDirectory().toFile()).start();
                cloudService.setProcess(process);
            } catch (IOException e) {
                e.printStackTrace();
            }

             */
        }
    }

    @Override
    public void stop(CloudService service) {

    }


    private List<String> arguments(LocalCloudService service) {
        final var wrapper = Node.getInstance().getRuntimeConfiguration().getNodePath().getStoragePath().toAbsolutePath();
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

        final var applicationFile = service.getDirectory().resolve(group.getGroupType().getPath(Node.getInstance().getRuntimeConfiguration().getNodePath().getStoragePath()));

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

        //todo i dont know but i think this is the ip of the node
        arguments.add("127.0.0.1");
        arguments.add(String.valueOf(Node.getInstance().getRuntimeConfiguration().getPort()));

        arguments.add(String.valueOf(service.getPort()));
        return arguments;
    }
}
