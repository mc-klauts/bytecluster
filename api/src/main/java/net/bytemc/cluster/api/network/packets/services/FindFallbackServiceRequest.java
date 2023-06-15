package net.bytemc.cluster.api.network.packets.services;

import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Packet.Info(id = 14)
public final class FindFallbackServiceRequest extends Packet {

    @Override
    public void read(PacketBuffer reader) {
        //nothing
    }

    @Override
    public void write(PacketBuffer writer) {

    }
}
