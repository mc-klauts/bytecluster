package net.bytemc.cluster.node.services;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.packets.services.WrapperRequestServiceCommandPacket;
import net.bytemc.cluster.api.network.packets.services.WrapperRequestServiceShutdownPacket;
import net.bytemc.cluster.node.Node;

public final class CloudServicePacketListener {

    public CloudServicePacketListener() {
        Node.getInstance().getPacketPool().registerListener(WrapperRequestServiceCommandPacket.class, (channel, packet) -> {
            var service = Cluster.getInstance().getServiceProvider().findService(packet.getServiceId());
            if(service == null) {
                return;
            }
            service.executeCommand(packet.getCommand());
        });
        Node.getInstance().getPacketPool().registerListener(WrapperRequestServiceShutdownPacket.class, (channel, packet) -> {
            var service = Cluster.getInstance().getServiceProvider().findService(packet.getServiceId());
            if(service == null) {
                return;
            }
            service.shutdown();
        });
    }
}
