package net.bytemc.cluster.api.network.packets.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.CloudServiceGroup;

@Getter
@AllArgsConstructor
@Packet.Info(id = 61)
public final class SingletonGroupResponse extends Packet {
    private CloudServiceGroup serviceGroup;

    @Override
    public void read(PacketBuffer reader) {
        this.serviceGroup = Cluster.getInstance().getServiceGroupProvider().getCloudServiceGroupFromBuffer(reader);
    }

    @Override
    public void write(PacketBuffer writer) {
        PacketBufferHelper.writeCloudServiceGroup(writer, serviceGroup);
    }
}
