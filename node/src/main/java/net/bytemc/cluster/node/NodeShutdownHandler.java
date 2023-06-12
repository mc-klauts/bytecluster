package net.bytemc.cluster.node;

import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.node.logger.NodeLogger;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;
import org.jetbrains.annotations.NotNull;

public final class NodeShutdownHandler {

    public static void shutdown(@NotNull Node node) {
        if (node.isRunning()) {
            node.setRunning(false);

            Logger.info("Shutdown all services...");

            for (var service : node.getServiceProvider().findServices()) {
                service.shutdown();
            }

            ((CloudServiceProviderImpl) node.getServiceProvider()).getQueue().shutdown();

            //disable and shutdown console process
            node.getConsoleTerminal().close();
            node.getClusterNetwork().close();
            System.exit(-1);
        }
    }
}
