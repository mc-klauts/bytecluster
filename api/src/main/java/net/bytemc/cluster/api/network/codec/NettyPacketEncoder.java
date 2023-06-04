package net.bytemc.cluster.api.network.codec;

import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.MessageToByteEncoder;
import lombok.NonNull;
import net.bytemc.cluster.api.network.Packet;

public final class NettyPacketEncoder extends MessageToByteEncoder<Packet> {

    public static final NettyPacketEncoder INSTANCE = new NettyPacketEncoder();

    @Override
    protected Buffer allocateBuffer(@NonNull ChannelHandlerContext ctx, @NonNull Packet msg) {
        return ctx.bufferAllocator().allocate(0);
    }

    @Override
    protected void encode(@NonNull ChannelHandlerContext ctx, @NonNull Packet msg, @NonNull Buffer out) {
        /*
                final var buf = new NetworkBuf(out);

        int packetId = PacketRegistry.getInstance().getPacketId(packet);
        buf.writeVarInt(packetId);

        if(PacketDebugger.isEnable()) {
            System.out.println("Outgoing packet: (id=" + packetId + ") " + packet.getClass().getSimpleName());
        }

        packet.write(buf);

         */
    }

    @Override
    public boolean isSharable() {
        return true;
    }
}