package net.bytemc.bytecluster.wrapper.network;

import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.network.Packet;
import java.io.IOException;

@RequiredArgsConstructor
public final class NettyNetworkClientHandler extends SimpleChannelInboundHandler<Packet> {

    private final String instanceName;
    private final NettyClient nettyClient;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet packet) {

    }

    @Override
    public void channelExceptionCaught(@NonNull ChannelHandlerContext ctx, @NonNull Throwable cause) {
        if (!(cause instanceof IOException)) {
            cause.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // authorize instance
        nettyClient.setChannel(ctx.channel());
        System.out.println("finished");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelReadComplete(@NonNull ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
