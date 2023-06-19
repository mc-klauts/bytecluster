package net.bytemc.cluster.node.groups;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.packets.groups.WrapperRequestRemoveGroupPacket;

public final class CloudServiceGroupPacketListener {

    public CloudServiceGroupPacketListener() {
        Cluster.getInstance().getPacketPool().registerListener(WrapperRequestRemoveGroupPacket.class, (channel, packet) -> {
            Cluster.getInstance().getServiceGroupProvider().removeGroup(packet.getServerId());
        });
    }

}
