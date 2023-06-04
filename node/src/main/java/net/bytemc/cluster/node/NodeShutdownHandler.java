package net.bytemc.cluster.node;

public final class NodeShutdownHandler {

    public static void shutdown(Node node) {

        //disable and shutdown console process
        node.getConsoleTerminal().close();

        node.getClusterNetwork().close();
    }
}
