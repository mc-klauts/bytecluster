package net.bytemc.cluster.api.network.codec;

import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.ByteToMessageDecoder;
import lombok.NonNull;
import net.bytemc.cluster.api.network.NettyUtils;
public final class VarInt32FrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(@NonNull ChannelHandlerContext ctx, @NonNull Buffer in) {
        // ensure that the channel we're reading from is still open
        if (!ctx.channel().isActive()) {
            return;
        }

        var readerIndex = in.readerOffset();

        // try to read the full message length from the buffer, reset the buffer if we've read nothing
        var length = NettyUtils.readVarIntOrNull(in);
        if (length == null || readerIndex == in.readerOffset()) {
            in.readerOffset(readerIndex);
            return;
        }

        // skip empty packets silently
        if (length <= 0) {
            // check if there are bytes to skip
            if (in.readableBytes() > 0) {
                in.skipReadableBytes(in.readableBytes());
            }
            return;
        }

        // check if the packet data supplied in the buffer is actually at least the transmitted size
        if (in.readableBytes() >= length) {
            // fire the channel read
            ctx.fireChannelRead(in.copy(in.readerOffset(), length, true));
            in.skipReadableBytes(length);
        } else {
            // reset the reader index, there is still data missing
            in.readerOffset(readerIndex);
        }
    }
}