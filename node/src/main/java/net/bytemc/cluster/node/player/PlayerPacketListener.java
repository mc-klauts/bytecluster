package net.bytemc.cluster.node.player;

import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerConnectPacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerDisconnectPacket;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;

public final class PlayerPacketListener {

    public PlayerPacketListener() {


        Node.getInstance().getPacketPool().registerListener(CloudPlayerConnectPacket.class, (channel, packet) -> {
            String name = ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider()).getServiceByConnection(channel).getName();
            //todo
        });

        Node.getInstance().getPacketPool().registerListener(CloudPlayerDisconnectPacket.class, (channel, packet) -> {
            //todo
        });
    }
}
