package net.bytemc.bytecluster.wrapper.network;

import io.netty5.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.network.codec.ClusterChannelInboundHandler;
import net.bytemc.cluster.api.network.packets.ServiceIdentifiyPacket;

@RequiredArgsConstructor
public final class NettyNetworkClientHandler extends ClusterChannelInboundHandler {

    private final String instanceName;
    private final NettyClient nettyClient;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // authorize instance
        nettyClient.setChannel(ctx.channel());
        ctx.channel().writeAndFlush(new ServiceIdentifiyPacket(instanceName));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

    }
}
