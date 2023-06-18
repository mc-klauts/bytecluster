package net.bytemc.cluster.node.network.netty;

import io.netty5.channel.ChannelHandlerContext;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.services.CloudServiceConnectEvent;
import net.bytemc.cluster.api.event.services.CloudServiceShutdownEvent;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.codec.ClusterChannelInboundHandler;
import net.bytemc.cluster.api.network.packets.ServiceIdentifiyPacket;
import net.bytemc.cluster.api.service.CloudServiceState;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;
import net.bytemc.cluster.node.services.LocalCloudService;

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

            if (service instanceof LocalCloudService localCloudService) {
                localCloudService.setState(CloudServiceState.ONLINE);
            }
            Logger.info("Service " + serviceIdentifiyPacket.getId() + " is online and connected to the node&8.");
            // call on all instances of the node
            Cluster.getInstance().getEventHandler().call(new CloudServiceConnectEvent(service));
        } else {
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        var serviceHandler = ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider());
        var service = serviceHandler.getServiceByConnection(ctx.channel());

        if (service != null && service instanceof LocalCloudService cloudService) {
            cloudService.shutdown();
            Logger.info("Service " + service.getName() + " is offline and disconnected from the node&8.");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        var hostname = ctx.channel().remoteAddress().toString().split(":")[0].replace("/", "");

        if(Node.getInstance().getRuntimeConfiguration().getAllowedHosts().contains(hostname)) {
            //all fine
            return;
        }

        ctx.close();
        Logger.warn("A service tried to connect to the node, but the hostname is not allowed&8. &7(" + hostname + ")");
    }
}