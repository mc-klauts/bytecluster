package net.bytemc.cluster.plugin.velocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerConnectPacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerDisconnectPacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerSwitchPacket;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public final class VelocityListener {

    private ProxyServer proxyServer;

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final PlayerChooseInitialServerEvent event) {
        Cluster.getInstance().getServiceProvider().findFallbackId().ifPresentOrElse(id -> {
            // find the next filtered fallback
            event.setInitialServer(this.proxyServer.getServer(id).get());
        }, () -> {
            // no fallback are found
            event.setInitialServer(null);
        });
    }

    @Subscribe
    public void handleConnect(ServerPostConnectEvent event) {
        if (event.getPreviousServer() == null) {
            // we think first connect :)
            var currentServer = event.getPlayer().getCurrentServer().orElse(null);
            if (currentServer == null) {
                event.getPlayer().disconnect(Component.text("§cYou are not connected to a server!"));
                return;
            }
            Wrapper.getInstance().sendPacket(new CloudPlayerConnectPacket(event.getPlayer().getUsername(), event.getPlayer().getUniqueId(), currentServer.getServerInfo().getName()));
        } else {
            // switch server
            Wrapper.getInstance().sendPacket(new CloudPlayerSwitchPacket(event.getPlayer().getUniqueId(),
                    event.getPreviousServer().getServerInfo().getName(),
                    event.getPlayer().getCurrentServer().get().getServerInfo().getName()));
        }
    }

    @Subscribe
    public void handleDisconnect(@NonNull DisconnectEvent event) {
        if(event.getLoginStatus() == DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN || event.getLoginStatus() == DisconnectEvent.LoginStatus.PRE_SERVER_JOIN) {
            // only the single
            Wrapper.getInstance().sendPacket(new CloudPlayerDisconnectPacket(event.getPlayer().getUniqueId()));
        }
    }

    @Subscribe
    public void handleKick(KickedFromServerEvent event) {
        //todo
    }
}
