package net.bytemc.cluster.node.player;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.PacketPool;
import net.bytemc.cluster.api.network.packets.player.*;
import net.bytemc.cluster.api.player.events.CloudPlayerConnectEvent;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;

public final class PlayerPacketListener {

    public PlayerPacketListener() {
        PacketPool pool = Node.getInstance().getPacketPool();
        pool.registerListener(CloudPlayerConnectPacket.class, (channel, packet) -> {
            var proxyName = ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider()).getServiceByConnection(channel).getName();

            Node.getInstance().getPlayerHandler().registerCloudPlayer(packet.getUuid(), packet.getUsername(), packet.getCurrentServer(), proxyName);
            Node.getInstance().getEventHandler().call(new CloudPlayerConnectEvent(Node.getInstance().getPlayerHandler().findPlayer(packet.getUuid()).get()));
        });

        pool.registerListener(CloudPlayerDisconnectPacket.class, (channel, packet) -> {
            Node.getInstance().getPlayerHandler().unregisterCloudPlayer(packet.getUniqueId());
            //todo call events
        });

        pool.registerListener(CloudPlayerSwitchPacket.class, (channel, packet) -> {
            var player = Cluster.getInstance().getPlayerHandler().findPlayer(packet.getUniqueId());
            if (player.isPresent()) {
                if (player.get() instanceof LocalCloudPlayer localCloudPlayer) {
                    localCloudPlayer.setCurrentServer(Cluster.getInstance().getServiceProvider().findService(packet.getCurrentServer()));
                }
            }
            //todo call events
        });

        // if lobby will be implemented, this packet will be used
        pool.registerListener(CloudPlayerTablistPacket.class, (channel, packet) -> {
            var player = Cluster.getInstance().getPlayerHandler().findPlayer(packet.getUuid());
            if (player.isPresent()) {
                player.get().sendTablist(packet.getHeader(), packet.getFooter());
            }
        });

        pool.registerListener(CloudPlayerSendMessagePacket.class, (channel, packet) -> {
            var player = Cluster.getInstance().getPlayerHandler().findPlayer(packet.getUuid());
            if (player.isPresent()) {
                player.get().sendMessage(packet.getMessage());
            }
        });

        pool.registerListener(CloudPlayerRequestKickPacket.class, (channel, packet) -> {
            var player = Cluster.getInstance().getPlayerHandler().findPlayer(packet.getUuid());
            if (player.isPresent()) {
                player.get().kick(packet.getReason());
            }
        });
    }
}
