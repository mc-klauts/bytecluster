package net.bytemc.cluster.api.network.codec;

import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.MessageToByteEncoder;
import lombok.NonNull;
import net.bytemc.cluster.api.network.NettyUtils;

public final class VarInt32FramePrepender extends MessageToByteEncoder<Buffer> {

    public static final VarInt32FramePrepender INSTANCE = new VarInt32FramePrepender();

    @Override
    protected Buffer allocateBuffer(@NonNull ChannelHandlerContext ctx, @NonNull Buffer buffer) {
        return ctx.bufferAllocator().allocate(Integer.numberOfLeadingZeros(buffer.readableBytes()) + buffer.readableBytes());
    }

    @Override
    protected void encode(@NonNull ChannelHandlerContext ctx, @NonNull Buffer msg, @NonNull Buffer out) {
        NettyUtils.writeVarInt(out, msg.readableBytes());
        out.writeBytes(msg);
    }

    @Override
    public boolean isSharable() {
        return true;
    }
}