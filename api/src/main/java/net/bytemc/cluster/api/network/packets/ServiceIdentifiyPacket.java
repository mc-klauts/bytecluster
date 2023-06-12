package net.bytemc.cluster.api.network.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 0)
public final class ServiceIdentifiyPacket extends Packet {

    private String id;

    @Override
    public void read(PacketBuffer reader) {
        this.id = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(id);
    }
}
