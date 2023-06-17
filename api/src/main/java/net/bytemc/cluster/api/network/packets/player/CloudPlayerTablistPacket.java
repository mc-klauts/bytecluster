package net.bytemc.cluster.api.network.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Packet.Info(id = 39)
public final class CloudPlayerTablistPacket extends Packet {

    private UUID uuid;
    private String header;
    private String footer;

    @Override
    public void read(PacketBuffer reader) {
        this.uuid = reader.readUUID();
        this.header = reader.readString();
        this.footer = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeUUID(uuid);
        writer.writeString(header);
        writer.writeString(footer);
    }
}
