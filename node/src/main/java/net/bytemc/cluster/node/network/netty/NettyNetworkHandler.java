package net.bytemc.cluster.node.network.netty;

import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import lombok.NonNull;
import net.bytemc.cluster.api.network.Packet;

import java.io.IOException;

public final class NettyNetworkHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet object) {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channel) {
        channel.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelActive(ChannelHandlerContext channel) {

    }

    @Override
    public void channelExceptionCaught(@NonNull ChannelHandlerContext ctx, @NonNull Throwable cause) {
        if (!(cause instanceof IOException)) {
            cause.printStackTrace();
        }
    }
}