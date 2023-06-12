package net.bytemc.cluster.api.event.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.AbstractCommunicatableEvent;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.CloudService;

@Getter
@AllArgsConstructor
public final class CloudServiceConnectEvent extends AbstractCommunicatableEvent {

    private CloudService service;

    @Override
    public void read(PacketBuffer reader) {
        this.service = Cluster.getInstance().getServiceProvider().getCloudServiceByBuffer(reader);
    }

    @Override
    public void write(PacketBuffer writer) {
        PacketBufferHelper.writeService(writer, service);
    }
}
