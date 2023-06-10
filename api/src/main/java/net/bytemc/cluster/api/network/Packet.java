package net.bytemc.cluster.api.network;

import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Packet {

    public abstract void read(PacketBuffer reader);

    public abstract void write(PacketBuffer writer);

    public int id() {
        return this.getClass().getAnnotation(Info.class).id();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        int id();
    }
}
