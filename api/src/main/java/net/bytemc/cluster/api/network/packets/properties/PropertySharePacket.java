package net.bytemc.cluster.api.network.packets.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 80)
public final class PropertySharePacket extends Packet {

    private Class<?> type;
    private String propertyAsString;

    @Override
    public void read(PacketBuffer reader) {
        try {
            this.type = Class.forName(reader.readString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.propertyAsString = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(this.type.getName());
        writer.writeString(this.propertyAsString);
    }
}
