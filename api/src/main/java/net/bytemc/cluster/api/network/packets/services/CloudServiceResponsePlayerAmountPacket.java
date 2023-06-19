package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 52)
public final class CloudServiceResponsePlayerAmountPacket extends Packet {

    private int amount;

    @Override
    public void read(PacketBuffer reader) {
        amount = reader.readInt();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeInt(amount);
    }
}
