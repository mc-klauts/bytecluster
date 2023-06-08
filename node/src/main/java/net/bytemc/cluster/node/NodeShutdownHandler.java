package net.bytemc.cluster.node;

import org.jetbrains.annotations.NotNull;

public final class NodeShutdownHandler {

    public static void shutdown(@NotNull Node node) {

        node.setRunning(false);

        //disable and shutdown console process
        node.getConsoleTerminal().close();

        node.getClusterNetwork().close();
    }
}
