package net.bytemc.cluster.node.cluster;

import net.bytemc.cluster.node.configuration.RuntimeConfiguration;
import net.bytemc.cluster.node.logger.Logger;
import net.bytemc.cluster.node.network.netty.NettyServer;

public final class ClusterNetwork {

    private NettyServer nettyServer;

    public ClusterNetwork(RuntimeConfiguration configuration) {
        this.nettyServer = new NettyServer();

        this.nettyServer.initialize(configuration.getPort()).onComplete(unused -> {
            Logger.info("Netty server initialize successfully.");
        }).onCancel(s -> {


            // todo : load modules

        });
    }

    public void close() {
        this.nettyServer.close();
    }

}
