package net.bytemc.cluster.api.network.codec;

import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.ByteToMessageDecoder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.misc.UnsafeAccess;
import net.bytemc.cluster.api.network.NettyUtils;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.PacketPool;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.lang.reflect.InvocationTargetException;

public final class NettyPacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(@NonNull ChannelHandlerContext ctx, @NonNull Buffer in) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // validates that the channel associated to this decoder call is still active and actually transferred data before
        if (!ctx.channel().isActive() || in.readableBytes() <= 0) {
            return;
        }

        final var buf = new PacketBuffer(in);
        final var id = buf.readInt();

        final var packetClass = PacketPool.getPacketClass(id);
        final Packet packet = UnsafeAccess.allocate(packetClass);

        packet.read(buf);
        ctx.fireChannelRead(packet);
    }
}
