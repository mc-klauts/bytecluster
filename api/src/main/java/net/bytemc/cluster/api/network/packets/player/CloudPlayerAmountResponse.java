package net.bytemc.cluster.api.network.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 36)
public final class CloudPlayerAmountResponse extends Packet {

    private int onlineCount;

    @Override
    public void read(PacketBuffer reader) {
        this.onlineCount = reader.readInt();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeInt(onlineCount);
    }
}
