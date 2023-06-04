package net.bytemc.cluster.launcher.misc;

public final class JavaVersionHelper {

    public static int getJavaVersionId() {
        return Integer.parseInt(System.getProperty("java.specification.version"));
    }
}
