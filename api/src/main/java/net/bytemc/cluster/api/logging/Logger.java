package net.bytemc.cluster.api.logging;

import net.bytemc.cluster.api.Cluster;

public interface Logger {

    void logInfo(String text);

    void logWarn(String text);

    void logError(String text, Throwable exception);

    void logEmpty(String line);

    static void empty(String line) {
        Cluster.getInstance().getLogger().logEmpty(line);
    }

    static void info(String text) {
        Cluster.getInstance().getLogger().logInfo(text);
    }

    static void warn(String text) {
        Cluster.getInstance().getLogger().logWarn(text);
    }

    static void error(String text, Throwable exception) {
        Cluster.getInstance().getLogger().logError(text, exception);
    }

}
