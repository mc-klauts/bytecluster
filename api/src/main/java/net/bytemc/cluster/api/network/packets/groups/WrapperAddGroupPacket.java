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
@Packet.Info(id = 67)
public final class WrapperAddGroupPacket extends Packet {

    private CloudServiceGroup group;

    @Override
    public void read(PacketBuffer reader) {
        group = Cluster.getInstance().getServiceGroupProvider().getCloudServiceGroupFromBuffer(reader);
    }

    @Override
    public void write(PacketBuffer writer) {
        PacketBufferHelper.writeCloudServiceGroup(writer, this.group);
    }
}
