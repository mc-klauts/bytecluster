package net.bytemc.cluster.node.network.netty;

import io.netty5.channel.Channel;
import lombok.NonNull;
import net.bytemc.cluster.api.network.codec.NettyPacketDecoder;
import net.bytemc.cluster.api.network.codec.NettyPacketEncoder;
import net.bytemc.cluster.api.network.codec.VarInt32FrameDecoder;
import net.bytemc.cluster.api.network.codec.VarInt32FramePrepender;

public final class NettyNetworkServerInitializer extends NettyOptionSettingChannelInitializer {

    @Override
    protected void doInitChannel(@NonNull Channel channel) {

        channel.pipeline()
                .addLast("packet-length-deserializer", new VarInt32FrameDecoder())
                 .addLast("packet-decoder", new NettyPacketDecoder())
                .addLast("packet-length-serializer", VarInt32FramePrepender.INSTANCE)
                .addLast("packet-encoder", NettyPacketEncoder.INSTANCE)
                .addLast("network-server-handler", new NettyNetworkHandler());
    }
}
