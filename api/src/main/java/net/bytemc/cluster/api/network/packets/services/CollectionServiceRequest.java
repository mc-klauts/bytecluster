package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 12)
public final class CollectionServiceRequest extends Packet {

    private Filter serviceFilter;

    @Override
    public void read(PacketBuffer reader) {
        this.serviceFilter = reader.readEnum(Filter.class);
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeEnum(serviceFilter);
    }

    public enum Filter {
        ALL
    }
}
