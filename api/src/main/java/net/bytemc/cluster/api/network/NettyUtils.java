package net.bytemc.cluster.api.network;

import io.netty5.buffer.Buffer;
import io.netty5.buffer.BufferUtil;
import io.netty5.handler.codec.DecoderException;
import io.netty5.util.ResourceLeakDetector;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import sun.misc.Unsafe;

public final class NettyUtils {

    private static final int[] VAR_INT_BYTE_LENGTHS = new int[33];

    @SuppressWarnings("sunapi")
    public static final Unsafe unsafe;

    static {
        if (System.getProperty("io.netty5.leakDetection.level") == null) {
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
        }
        for (var i = 0; i <= 32; ++i) {
            VAR_INT_BYTE_LENGTHS[i] = (int) Math.ceil((31d - (i - 1)) / 7d);
        }
        VAR_INT_BYTE_LENGTHS[32] = 1;

        try {
            var field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NonNull Buffer writeVarInt(@NonNull Buffer buffer, int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                buffer.writeByte((byte) value);
                return buffer;
            } else {
                buffer.writeByte((byte) ((value & 0x7F) | 0x80));
                value >>>= 7;
            }
        }
    }

    public static @Nullable Integer readVarIntOrNull(@NonNull Buffer buffer) {
        var i = 0;
        var maxRead = Math.min(5, buffer.readableBytes());
        for (var j = 0; j < maxRead; j++) {
            var nextByte = buffer.readByte();
            i |= (nextByte & 0x7F) << j * 7;
            if ((nextByte & 0x80) != 128) {
                return i;
            }
        }
        return null;
    }

    public static int readVarInt(@NonNull Buffer buffer) {
        var varInt = readVarIntOrNull(buffer);
        if (varInt == null) {
            // unable to decode a var int at the current position
            var bufferDump = BufferUtil.hexDump(buffer, 0, buffer.readableBytes());
            throw new DecoderException(String.format(
                    "Unable to decode VarInt at current buffer position (%d): %s",
                    buffer.readerOffset(),
                    bufferDump));
        }

        return varInt;
    }

    public static int varIntBytes(int contentLength) {
        return VAR_INT_BYTE_LENGTHS[Integer.numberOfLeadingZeros(contentLength)];
    }

    public static Object initializeClass(Class<?> clazz) {
        try {
            return unsafe.allocateInstance(clazz);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
