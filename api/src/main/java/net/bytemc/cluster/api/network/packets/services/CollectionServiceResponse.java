package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.CloudService;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor
@Packet.Info(id = 13)
public final class CollectionServiceResponse extends Packet {

    private Collection<CloudService> services;

    @Override
    public void read(PacketBuffer reader) {
        int length = reader.readInt();
        var services = new ArrayList<CloudService>();
        for (int i = 0; i < length; i++) {
            services.add(Cluster.getInstance().getServiceProvider().getCloudServiceByBuffer(reader));
        }
        this.services = services;
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeInt(services.size());
        for (var service : this.services) {
            PacketBufferHelper.writeService(writer, service);
        }
    }
}
