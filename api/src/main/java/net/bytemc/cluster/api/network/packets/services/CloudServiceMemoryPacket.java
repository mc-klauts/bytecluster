package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 90)
public final class CloudServiceMemoryPacket extends Packet {
    private int memory;

    @Override
    public void read(PacketBuffer reader) {
        memory = reader.readInt();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeInt(memory);
    }
}
