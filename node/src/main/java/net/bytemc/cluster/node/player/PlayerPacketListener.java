package net.bytemc.cluster.node.player;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.PacketPool;
import net.bytemc.cluster.api.network.packets.player.*;
import net.bytemc.cluster.api.player.CloudPlayer;
import net.bytemc.cluster.api.player.events.CloudPlayerConnectEvent;
import net.bytemc.cluster.api.player.events.CloudPlayerDisconnectEvent;
import net.bytemc.cluster.api.player.events.CloudPlayerSwitchEvent;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;

import java.util.Optional;

public final class PlayerPacketListener {

    public PlayerPacketListener() {
        PacketPool pool = Node.getInstance().getPacketPool();
        pool.registerListener(CloudPlayerConnectPacket.class, (channel, packet) -> {
            var proxyName = ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider()).getServiceByConnection(channel).getName();

            Node.getInstance().getPlayerHandler().registerCloudPlayer(packet.getUuid(), packet.getUsername(), packet.getCurrentServer(), proxyName);
            Node.getInstance().getEventHandler().call(new CloudPlayerConnectEvent(Node.getInstance().getPlayerHandler().findPlayer(packet.getUuid()).get()));
        });

        pool.registerListener(CloudPlayerDisconnectPacket.class, (channel, packet) -> {
            Optional<CloudPlayer> player = Cluster.getInstance().getPlayerHandler().findPlayer(packet.getUniqueId());
            if (player.isPresent()) {
                Node.getInstance().getEventHandler().call(new CloudPlayerDisconnectEvent(player.get()));
                Node.getInstance().getPlayerHandler().unregisterCloudPlayer(packet.getUniqueId());
            }
        });

        pool.registerListener(CloudPlayerSwitchPacket.class, (channel, packet) -> {
            var player = Cluster.getInstance().getPlayerHandler().findPlayer(packet.getUniqueId());
            if (player.isPresent()) {
                if (player.get() instanceof LocalCloudPlayer localCloudPlayer) {
                    localCloudPlayer.setCurrentServer(Cluster.getInstance().getServiceProvider().findService(packet.getCurrentServer()));
                }
            }
            Node.getInstance().getEventHandler().call(new CloudPlayerSwitchEvent(Cluster.getInstance().getPlayerHandler().findPlayer(packet.getUniqueId()).get(), packet.getPreviousServer(), packet.getCurrentServer()));
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
