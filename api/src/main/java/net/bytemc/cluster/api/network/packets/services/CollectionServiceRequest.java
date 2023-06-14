package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.filter.CloudServiceFilter;

@Getter
@AllArgsConstructor
@Packet.Info(id = 12)
public final class CollectionServiceRequest extends Packet {

    private CloudServiceFilter serviceFilter;

    @Override
    public void read(PacketBuffer reader) {
        this.serviceFilter = reader.readEnum(CloudServiceFilter.class);
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeEnum(serviceFilter);
    }
}
