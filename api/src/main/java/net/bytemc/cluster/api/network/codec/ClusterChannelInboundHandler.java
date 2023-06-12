package net.bytemc.cluster.api.network.codec;

import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import lombok.NonNull;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.QueryPacket;

import java.io.IOException;

public abstract class ClusterChannelInboundHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
        if (packet instanceof QueryPacket queryPacket) {
            if (Cluster.getInstance().getPacketPool().isResponsePresent(queryPacket.getId())) {

            } else {

            }

        } else {
            // todo call packet listener
        }
    }

    @Override
    public void channelExceptionCaught(@NonNull ChannelHandlerContext ctx, @NonNull Throwable cause) {
        // ignore this exception
        if (!(cause instanceof IOException)) {
            cause.printStackTrace();
        }
    }
}
