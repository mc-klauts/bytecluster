package net.bytemc.cluster.api.network.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Packet.Info(id = 55)
public final class CloudPlayerSendServicePacket extends Packet {

    private UUID uuid;
    private String serviceId;

    @Override
    public void read(PacketBuffer reader) {
        uuid = reader.readUUID();
        serviceId = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeUUID(uuid);
        writer.writeString(serviceId);
    }
}
