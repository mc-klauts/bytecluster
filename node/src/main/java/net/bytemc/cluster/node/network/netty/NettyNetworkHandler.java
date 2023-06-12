package net.bytemc.cluster.node.network.netty;

import io.netty5.channel.ChannelHandlerContext;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.services.CloudServiceConnectEvent;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.codec.ClusterChannelInboundHandler;
import net.bytemc.cluster.api.network.packets.ServiceIdentifiyPacket;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.logger.NodeLogger;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;

public final class NettyNetworkHandler extends ClusterChannelInboundHandler {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {

        var serviceHandler = ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider());

        if (serviceHandler.isConnectionVerified(ctx.channel())) {
            super.messageReceived(ctx, packet);
            return;
        }

        if (packet instanceof ServiceIdentifiyPacket serviceIdentifiyPacket) {
            var service = serviceHandler.findService(serviceIdentifiyPacket.getId());
            if (service == null) {
                ctx.close();
                return;
            }
            serviceHandler.addServiceConnection(ctx.channel(), service);
            Logger.info("Service " + serviceIdentifiyPacket.getId() + " is online and connected to the node.");

            // call on all instances of the node
            Cluster.getInstance().getEventHandler().call(new CloudServiceConnectEvent(service));
        } else {
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

    }
}