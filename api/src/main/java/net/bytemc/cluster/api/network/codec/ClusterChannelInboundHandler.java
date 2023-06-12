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
            var pool = Cluster.getInstance().getPacketPool();
            if (pool.isResponsePresent(queryPacket.getId())) {
                pool.callResponseRecievd(queryPacket.getId(), queryPacket.getPacket());
            } else {
                if (pool.isQueryModificationPresent(queryPacket.getPacket().getClass())) {
                    ctx.channel().writeAndFlush(new QueryPacket(queryPacket.getId(), pool.callQueryModification(queryPacket.getPacket())));
                }
            }
        } else {
            Cluster.getInstance().getPacketPool().callPacketListener(ctx.channel(), packet);
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
