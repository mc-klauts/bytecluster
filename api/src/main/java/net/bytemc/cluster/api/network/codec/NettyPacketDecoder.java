package net.bytemc.cluster.api.network.codec;

import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.ByteToMessageDecoder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.network.NettyUtils;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@RequiredArgsConstructor
public final class NettyPacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(@NonNull ChannelHandlerContext ctx, @NonNull Buffer in) {
        // validates that the channel associated to this decoder call is still active and actually transferred data before
        if (!ctx.channel().isActive() || in.readableBytes() <= 0) {
            return;
        }

        try {
            // read the required base data from the buffer
            var packetId = NettyUtils.readVarInt(in);

            // extract the body
            var bodyLength = NettyUtils.readVarInt(in);
            var body = new PacketBuffer(in.copy(in.readerOffset(), bodyLength, true));
            in.skipReadableBytes(bodyLength);

            /*
            // construct the packet
            var packetClass = PacketPool.getPacketClass(packetId);
            BasicPacket packet;

            if (packetClass.getDeclaredAnnotation(Packet.class).useNoArgConstructor()) {
                packet = packetClass.getConstructor().newInstance();
            } else {
                packet = (BasicPacket) NettyUtils.initializeClass(packetClass);
            }

            if (packet == null) {
                throw new NullPointerException("Packet is null with id " + packetId + " is null");
            }

             */

           // packet.setContent(body);
          //  packet.read(body);

            // register the packet for further downstream handling
           // ctx.fireChannelRead(packet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
