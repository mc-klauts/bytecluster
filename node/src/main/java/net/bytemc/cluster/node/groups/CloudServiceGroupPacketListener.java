package net.bytemc.cluster.node.groups;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.packets.groups.WrapperAddGroupPacket;
import net.bytemc.cluster.api.network.packets.groups.WrapperRequestRemoveGroupPacket;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;

public final class CloudServiceGroupPacketListener {

    public CloudServiceGroupPacketListener() {
        Cluster.getInstance().getPacketPool().registerListener(WrapperRequestRemoveGroupPacket.class, (channel, packet) -> {
            Cluster.getInstance().getServiceGroupProvider().removeGroup(packet.getServerId());
        });

        Cluster.getInstance().getPacketPool().registerListener(WrapperAddGroupPacket.class, (channel, packet) -> {
            Cluster.getInstance().getServiceGroupProvider().addGroup(packet.getGroup());

            //check queue
            ((CloudServiceProviderImpl)Cluster.getInstance().getServiceProvider()).getQueue().checkQueue();
        });

    }
}
