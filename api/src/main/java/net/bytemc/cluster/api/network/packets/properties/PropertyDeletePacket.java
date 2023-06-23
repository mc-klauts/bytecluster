package net.bytemc.cluster.api.network.packets.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 82)
public final class PropertyDeletePacket extends Packet {

    private String id;

    @Override
    public void read(PacketBuffer reader) {
        this.id = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(this.id);
    }
}
