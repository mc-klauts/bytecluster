package net.bytemc.cluster.api.network.packets.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 80)
public final class PropertySharePacket extends Packet {

    private String type;
    private String propertyAsString;

    @Override
    public void read(PacketBuffer reader) {
        this.type = reader.readString();
        this.propertyAsString = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(this.type);
        writer.writeString(this.propertyAsString);
    }
}
