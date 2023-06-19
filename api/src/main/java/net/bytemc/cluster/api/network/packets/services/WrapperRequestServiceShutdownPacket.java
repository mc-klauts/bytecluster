package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 53)
public final class WrapperRequestServiceShutdownPacket extends Packet {

    private String serviceId;

    @Override
    public void read(PacketBuffer reader) {
        this.serviceId = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(serviceId);
    }
}
