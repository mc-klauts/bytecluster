package net.bytemc.cluster.api.network.packets.services;

import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Packet.Info(id = 16)
public class CollectionServiceRequest extends Packet {
    @Override
    public void read(PacketBuffer reader) {

    }

    @Override
    public void write(PacketBuffer writer) {

    }
}
