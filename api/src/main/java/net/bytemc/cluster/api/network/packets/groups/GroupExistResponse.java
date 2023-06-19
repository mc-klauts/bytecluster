package net.bytemc.cluster.api.network.packets.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 65)
public final class GroupExistResponse extends Packet {

    private boolean exists;

    @Override
    public void read(PacketBuffer reader) {
        this.exists = reader.readBoolean();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeBoolean(exists);
    }
}
