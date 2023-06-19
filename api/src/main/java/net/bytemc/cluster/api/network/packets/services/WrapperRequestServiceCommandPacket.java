package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 50)
public final class WrapperRequestServiceCommandPacket extends Packet {

    private String serviceId;
    private String command;

    @Override
    public void read(PacketBuffer reader) {
        this.serviceId = reader.readString();
        this.command = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(serviceId);
        writer.writeString(command);
    }
}
