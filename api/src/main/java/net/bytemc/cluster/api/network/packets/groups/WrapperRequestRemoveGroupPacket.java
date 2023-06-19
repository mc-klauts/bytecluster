package net.bytemc.cluster.api.network.packets.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 66)
public final class WrapperRequestRemoveGroupPacket extends Packet {
    private String serverId;

    @Override
    public void read(PacketBuffer reader) {
        this.serverId = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(serverId);
    }
}
