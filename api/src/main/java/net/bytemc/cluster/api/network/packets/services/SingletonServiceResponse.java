package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.CloudService;

@Getter
@AllArgsConstructor
@Packet.Info(id = 11)
public final class SingletonServiceResponse extends Packet {

    private CloudService cloudService;

    @Override
    public void read(PacketBuffer reader) {
        this.cloudService = Cluster.getInstance().getServiceProvider().getCloudServiceByBuffer(reader);
    }

    @Override
    public void write(PacketBuffer writer) {
        PacketBufferHelper.writeService(writer, cloudService);
    }
}
