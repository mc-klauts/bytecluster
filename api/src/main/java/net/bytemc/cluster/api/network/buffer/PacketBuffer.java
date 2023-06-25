package net.bytemc.cluster.api.network.buffer;

import io.netty5.buffer.Buffer;
import io.netty5.buffer.DefaultBufferAllocators;
import lombok.Getter;
import net.bytemc.cluster.api.network.NettyUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class PacketBuffer {

    @Getter
    private final Buffer buf;

    public PacketBuffer(@NotNull Buffer buf) {
        this.buf = buf;
    }

    public PacketBuffer() {
        this.buf = DefaultBufferAllocators.offHeapAllocator().allocate(0);
    }

    public PacketBuffer writeString(final @NotNull String string) {
        final var bytes = string.getBytes(StandardCharsets.UTF_8);
        this.writeInt(bytes.length);
        buf.writeCharSequence(string, StandardCharsets.UTF_8);
        return this;
    }

    public String readString() {
        return buf.readCharSequence(NettyUtils.readVarIntOrNull(buf), StandardCharsets.UTF_8).toString();
    }

    public <T extends Enum<T>> T readEnum(final @NotNull Class<T> enumClass) {
        final var i = this.readInt();
        if (i != -1) {
            return enumClass.getEnumConstants()[i];
        }
        return null;
    }

    public @NotNull PacketBuffer writeEnum(final Enum<?> val) {
        this.writeInt(val == null ? -1 : val.ordinal());
        return this;
    }

    public int readInt() {
        return NettyUtils.readVarIntOrNull(buf);
    }

    public double readDouble() {
        return buf.readDouble();
    }

    public @NotNull PacketBuffer writeDouble(double input) {
        buf.writeDouble(input);
        return this;
    }



    public @NotNull PacketBuffer writeInt(int input) {
        NettyUtils.writeVarInt(buf, input);
        return this;
    }

    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    public @NotNull PacketBuffer writeBoolean(boolean value) {
        this.buf.writeBoolean(value);
        return this;
    }

    public long readLong() {
        return this.buf.readLong();
    }

    public @NotNull PacketBuffer writeLong(long value) {
        this.buf.writeLong(value);
        return this;
    }

    public byte readByte() {
        return this.buf.readByte();
    }

    public @NotNull PacketBuffer writeByte(final byte b) {
        this.buf.writeByte(b);
        return this;
    }

    public UUID readUUID() {
        return new UUID(this.buf.readLong(), this.buf.readLong());
    }

    public @NotNull PacketBuffer writeUUID(final UUID uniqueId) {
        this.buf.writeLong(uniqueId.getMostSignificantBits());
        this.buf.writeLong(uniqueId.getLeastSignificantBits());
        return this;
    }

    public Collection<String> readStringCollection() {
        final var list = new ArrayList<String>();
        final var amount = this.readInt();
        for (int i = 0; i < amount; i++) {
            list.add(this.readString());
        }
        return list;
    }

    public @NotNull PacketBuffer writeStringCollection(final Collection<String> strings) {
        this.writeInt(strings.size());
        for (final var string : strings) {
            this.writeString(string);
        }
        return this;
    }

    public @NotNull PacketBuffer writeBytes(final @NotNull PacketBuffer buf) {
        this.buf.writeBytes(buf.buf);
        return this;
    }

    public @NotNull PacketBuffer writeBytes(final @NotNull byte[] bytes) {
        writeInt(bytes.length);
        for (byte aByte : bytes) {
            buf.writeByte(aByte);
        }
        return this;
    }

    public byte[] readByteArray() {
        int length = readInt();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = readByte();
        }
        return bytes;
    }

    public <T> @NotNull PacketBuffer writeList(final Collection<T> collection, final CustomEncoder<T> customEncoder) {
        writeInt(collection.size());
        for (T entry : collection) {
            customEncoder.write(entry, this);
        }
        return this;
    }

    public <T> @NotNull Collection<T> readCollection(final CustomDecoder<T> customDecoder) {
        int size = readInt();
        List<T> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(customDecoder.read(this));
        }
        return data;
    }

    public @NotNull PacketBuffer writeMap(Map<?, ?> map) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(map);

        writeBytes(byteArrayOutputStream.toByteArray());
        return this;
    }

    public @NotNull <K, V> Map<K, V> readMap(Class<K> key, Class<V> value) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(readByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Map<K, V>) objectInputStream.readObject();
    }

    public interface CustomDecoder<T> {
        T read(PacketBuffer buffer);
    }

    public interface CustomEncoder<T> {
        void write(T data, PacketBuffer buffer);
    }
}
