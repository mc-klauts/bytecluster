package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 91)
public final class CloudServiceMemoryRequestPacket extends Packet {

    private String serviceId;

    @Override
    public void read(PacketBuffer reader) {
        serviceId = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(serviceId);
    }
}
