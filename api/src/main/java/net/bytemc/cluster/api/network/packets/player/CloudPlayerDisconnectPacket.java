package net.bytemc.cluster.api.network.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Packet.Info(id = 31)
public final class CloudPlayerDisconnectPacket extends Packet {

    private UUID uniqueId;

    @Override
    public void read(PacketBuffer reader) {
        this.uniqueId = reader.readUUID();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeUUID(uniqueId);
    }
}
