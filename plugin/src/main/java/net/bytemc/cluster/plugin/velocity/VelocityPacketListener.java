package net.bytemc.cluster.plugin.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerTablistPacket;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class VelocityPacketListener {

    private static MiniMessage miniMessage = MiniMessage.miniMessage();

    public VelocityPacketListener(ProxyServer server) {
        Wrapper.getInstance().getPacketPool().registerListener(CloudPlayerTablistPacket.class, (channel, packet) -> {
            server.getPlayer(packet.getUuid()).ifPresent(player -> player.sendPlayerListHeaderAndFooter(miniMessage.deserialize(packet.getHeader()), miniMessage.deserialize(packet.getFooter())));
        });

    }
}
