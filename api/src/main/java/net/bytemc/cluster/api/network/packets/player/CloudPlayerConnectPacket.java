package net.bytemc.cluster.api.network.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Packet.Info(id = 30)
public final class CloudPlayerConnectPacket extends Packet {

    private String username;
    private UUID uuid;
    private String currentServer;

    @Override
    public void read(PacketBuffer reader) {
        this.username = reader.readString();
        this.uuid = reader.readUUID();
        this.currentServer = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(username);
        writer.writeUUID(uuid);
        writer.writeString(currentServer);
    }
}
