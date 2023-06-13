package net.bytemc.cluster.plugin.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class VelocityListener {

    private ProxyServer proxyServer;

    @Subscribe
    public void handle(final PlayerChooseInitialServerEvent event) {
        event.setInitialServer(this.proxyServer.getServer("lobby-1").get());
    }

}
