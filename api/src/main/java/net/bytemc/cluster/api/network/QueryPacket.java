package net.bytemc.cluster.api.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.bytemc.cluster.api.misc.UnsafeAccess;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Packet.Info(id = 1)
public final class QueryPacket extends Packet {

    private UUID id;
    @Setter
    private Packet packet;

    @Override
    public void write(@NotNull PacketBuffer buf) {
        buf.writeUUID(id);

        buf.writeInt(packet.id());
        packet.write(buf);
    }

    @Override
    public void read(@NotNull PacketBuffer buf) {
        this.id = buf.readUUID();
        var packetId = buf.readInt();

        this.packet = UnsafeAccess.allocate(PacketPool.getPacketClass(packetId));
        packet.read(buf);
    }
}
