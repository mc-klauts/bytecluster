package net.bytemc.cluster.api.network.codec;

import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.ByteToMessageDecoder;
import lombok.NonNull;
import net.bytemc.cluster.api.network.NettyUtils;
public final class VarInt32FrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(@NonNull ChannelHandlerContext ctx, @NonNull Buffer buf) {
        if (!ctx.channel().isActive()) {
            buf.close();
            return;
        }

        final var readerIndex = buf.readerOffset();
        final var length = NettyUtils.readVarInt(buf);

        if (readerIndex == buf.readerOffset()) {
            buf.readerOffset(readerIndex);
            return;
        }

        if (length <= 0) {
            if (buf.readableBytes() > 0) {
                buf.skipReadableBytes(buf.readableBytes());
            }
            return;
        }

        if (buf.readableBytes() >= length) {
            ctx.fireChannelRead(buf.copy(buf.readerOffset(), length, true));
            buf.skipReadableBytes(length);
        } else {
            buf.readerOffset(readerIndex);
        }
    }
}