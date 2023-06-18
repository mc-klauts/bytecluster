package net.bytemc.cluster.node.cluster;

import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.NodeShutdownHandler;
import net.bytemc.cluster.node.configuration.RuntimeConfiguration;
import net.bytemc.cluster.node.network.NettyServer;
import org.jetbrains.annotations.NotNull;

public final class ClusterNetwork {

    private final NettyServer nettyServer;

    public ClusterNetwork(@NotNull RuntimeConfiguration configuration) {
        this.nettyServer = new NettyServer();

        this.nettyServer.initialize(configuration.getPort()).onComplete(unused -> {
            Logger.info("Netty server initialize successfully.");
        }).onCancel(s -> {
            Logger.warn(s);
            NodeShutdownHandler.shutdown(Node.getInstance());
        });
    }

    public void close() {
        this.nettyServer.close();
    }

}
