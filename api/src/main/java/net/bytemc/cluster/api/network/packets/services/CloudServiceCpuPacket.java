package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@AllArgsConstructor
@Getter
@Packet.Info(id = 93)
public final class CloudServiceCpuPacket extends Packet {

    private double cpu;

    @Override
    public void read(PacketBuffer reader) {
        this.cpu = reader.readDouble();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeDouble(cpu);
    }
}
