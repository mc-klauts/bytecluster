package net.bytemc.cluster.api.network.packets.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 64)
public final class GroupExistRequest extends Packet {

    private String groupId;

    @Override
    public void read(PacketBuffer reader) {
        this.groupId = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(groupId);
    }
}
