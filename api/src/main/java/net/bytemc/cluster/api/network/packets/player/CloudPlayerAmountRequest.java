package net.bytemc.cluster.api.network.packets.player;

import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Packet.Info(id = 35)
public final class CloudPlayerAmountRequest extends Packet{

    @Override
    public void read(PacketBuffer reader) {
        // only here
    }

    @Override
    public void write(PacketBuffer writer) {

    }
}
