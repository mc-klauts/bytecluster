package net.bytemc.cluster.node.player;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerConnectPacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerDisconnectPacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerSwitchPacket;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;

public final class PlayerPacketListener {

    public PlayerPacketListener() {
        Node.getInstance().getPacketPool().registerListener(CloudPlayerConnectPacket.class, (channel, packet) -> {
            var proxyName = ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider()).getServiceByConnection(channel).getName();

            Node.getInstance().getPlayerHandler().registerCloudPlayer(packet.getUuid(), packet.getUsername(), packet.getCurrentServer(), proxyName);
            //todo call events
        });

        Node.getInstance().getPacketPool().registerListener(CloudPlayerDisconnectPacket.class, (channel, packet) -> {
            Node.getInstance().getPlayerHandler().unregisterCloudPlayer(packet.getUniqueId());
            //todo call events
        });

        Node.getInstance().getPacketPool().registerListener(CloudPlayerSwitchPacket.class, (channel, packet) -> {
            var player = Cluster.getInstance().getPlayerHandler().findPlayer(packet.getUniqueId());
            if (player.isPresent()) {
                if (player.get() instanceof LocalCloudPlayer localCloudPlayer) {
                    localCloudPlayer.setCurrentServer(Cluster.getInstance().getServiceProvider().findService(packet.getCurrentServer()));
                }
            }
            //todo call events
        });

    }
}
