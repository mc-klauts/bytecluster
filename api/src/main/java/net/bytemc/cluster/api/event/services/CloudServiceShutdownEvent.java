package net.bytemc.cluster.api.event.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.AbstractCommunicatableEvent;
import net.bytemc.cluster.api.event.CloudEvent;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.CloudService;

@Getter
@AllArgsConstructor
public final class CloudServiceShutdownEvent extends AbstractCommunicatableEvent {

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
