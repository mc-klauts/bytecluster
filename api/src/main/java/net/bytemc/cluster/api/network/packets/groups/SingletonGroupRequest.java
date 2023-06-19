package net.bytemc.cluster.api.network.packets.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 60)
public final class SingletonGroupRequest extends Packet {

    private String name;

    @Override
    public void read(PacketBuffer reader) {
        this.name = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(this.name);
    }
}
