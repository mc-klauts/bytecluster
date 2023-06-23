package net.bytemc.cluster.api.network.packets.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 81)
public final class PropertySetPacket extends Packet{

    private String id;
    private String propertyAsString;

    @Override
    public void read(PacketBuffer reader) {
        id = reader.readString();
        propertyAsString = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(id);
        writer.writeString(propertyAsString);
    }
}
