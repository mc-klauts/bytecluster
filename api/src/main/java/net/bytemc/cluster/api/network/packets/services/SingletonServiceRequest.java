package net.bytemc.cluster.api.network.packets.services;

import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Packet.Info(id = 10)
public final class SingletonServiceRequest extends Packet {

    @Override
    public void read(PacketBuffer reader) {
        // only send pull request
    }

    @Override
    public void write(PacketBuffer writer) {

    }
}
