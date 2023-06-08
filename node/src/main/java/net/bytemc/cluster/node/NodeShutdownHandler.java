package net.bytemc.cluster.node;

public final class NodeShutdownHandler {

    public static void shutdown(Node node) {

        node.setRunning(false);

        for (var service : node.getServiceProvider().findServices()) {
            service.shutdown();
        }

        //disable and shutdown console process
        node.getConsoleTerminal().close();

        node.getClusterNetwork().close();
    }
}
