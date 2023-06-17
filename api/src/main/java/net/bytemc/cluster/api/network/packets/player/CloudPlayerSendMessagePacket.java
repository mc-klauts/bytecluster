package net.bytemc.cluster.api.network.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Packet.Info(id = 39)
public final class CloudPlayerSendMessagePacket extends Packet {

    private UUID uuid;
    private String message;

    @Override
    public void read(PacketBuffer reader) {
        this.uuid = reader.readUUID();
        this.message = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeUUID(uuid);
        writer.writeString(message);
    }
}
