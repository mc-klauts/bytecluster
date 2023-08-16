package net.bytemc.cluster.plugin.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerRequestKickPacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerSendMessagePacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerSendServicePacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerTablistPacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class VelocityPacketListener {

    private static MiniMessage miniMessage = MiniMessage.miniMessage();

    public VelocityPacketListener(ProxyServer server) {
        var packetPool = Wrapper.getInstance().getPacketPool();
        packetPool.registerListener(CloudPlayerTablistPacket.class, (channel, packet) -> {
            server.getPlayer(packet.getUuid()).ifPresent(player -> {
                player.sendPlayerListHeaderAndFooter(miniMessage.deserialize(packet.getHeader()), miniMessage.deserialize(packet.getFooter()));
            });
        });
        packetPool.registerListener(CloudPlayerRequestKickPacket.class, (channel, packet) -> {
            server.getPlayer(packet.getUuid()).ifPresent(player -> player.disconnect(Component.text(packet.getReason())));
        });
        packetPool.registerListener(CloudPlayerSendMessagePacket.class, (channel, packet) -> {
            server.getPlayer(packet.getUuid()).ifPresent(player -> player.sendMessage(miniMessage.deserialize(packet.getMessage())));
        });
        packetPool.registerListener(CloudPlayerSendServicePacket.class, (channel, packet) -> {
            server.getPlayer(packet.getUuid()).ifPresent(player -> player.createConnectionRequest(server.getServer(packet.getServiceId()).get()).fireAndForget());
        });
    }
}
