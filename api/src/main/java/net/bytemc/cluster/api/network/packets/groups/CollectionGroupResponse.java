package net.bytemc.cluster.api.network.packets.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor
@Packet.Info(id = 62)
public final class CollectionGroupResponse extends Packet {

    private Collection<CloudServiceGroup> groups;

    @Override
    public void read(PacketBuffer reader) {
        int length = reader.readInt();
        var list = new ArrayList<CloudServiceGroup>();

        for (int i = 0; i < length; i++) {
            list.add(Cluster.getInstance().getServiceGroupProvider().getCloudServiceGroupFromBuffer(reader));
        }
        this.groups = list;
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeInt(this.groups.size());
        for (var group : groups) {
            PacketBufferHelper.writeCloudServiceGroup(writer, group);
        }
    }
}
