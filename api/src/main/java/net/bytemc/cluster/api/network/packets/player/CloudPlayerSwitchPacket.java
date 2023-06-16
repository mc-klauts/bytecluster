package net.bytemc.cluster.api.network.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Packet.Info(id = 32)
public final class CloudPlayerSwitchPacket extends Packet {

    private UUID uniqueId;
    private String previousServer;
    private String currentServer;

    @Override
    public void read(PacketBuffer reader) {
        this.uniqueId = reader.readUUID();
        this.previousServer = reader.readString();
        this.currentServer = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeUUID(uniqueId);
        writer.writeString(previousServer);
        writer.writeString(currentServer);
    }
}
