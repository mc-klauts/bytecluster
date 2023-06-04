package net.bytemc.cluster.launcher;

import net.bytemc.cluster.launcher.misc.JavaVersionHelper;
import net.bytemc.cluster.launcher.misc.ThreadHelper;

import java.nio.file.Path;

public final class ClusterBootstrap {

    public static void main(String[] args) {

        //check current java version is higher than 20
        if (JavaVersionHelper.getJavaVersionId() < 20) {
            System.out.println("Your java version is too low, please use java 20 or higher.");
            ThreadHelper.HANDLER.await(10).exit();
            return;
        }

        var workingPath = Path.of(System.getProperty("user.dir"));
        var lock = new ClusterLock();

        if (!lock.lock(workingPath)) {
            System.out.println("The launcher is already running, please close the launcher and try again.");
            ThreadHelper.HANDLER.await(10).exit();
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> lock.publishLock()));
        new ClusterLauncher(args);
    }
}
