package net.bytemc.bytecluster.wrapper.network;

import io.netty5.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.bytemc.cluster.api.network.NettyOptionSettingChannelInitializer;
import net.bytemc.cluster.api.network.codec.NettyPacketDecoder;
import net.bytemc.cluster.api.network.codec.NettyPacketEncoder;
import net.bytemc.cluster.api.network.codec.VarInt32FrameDecoder;
import net.bytemc.cluster.api.network.codec.VarInt32FramePrepender;

@AllArgsConstructor
public final class NettyNetworkClientInitializer extends NettyOptionSettingChannelInitializer {

    private final NettyClient nettyClient;
    private final String instanceName;

    @Override
    protected void doInitChannel(@NonNull Channel channel) {
        channel.pipeline()
                .addLast("packet-length-deserializer", new VarInt32FrameDecoder())
                .addLast("packet-decoder", new NettyPacketDecoder())
                .addLast("packet-length-serializer", VarInt32FramePrepender.INSTANCE)
                .addLast("packet-encoder", NettyPacketEncoder.INSTANCE)
                .addLast("network-client-handler", new NettyNetworkClientHandler(instanceName, nettyClient));
    }

}
