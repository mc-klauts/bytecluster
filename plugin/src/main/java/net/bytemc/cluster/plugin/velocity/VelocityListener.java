package net.bytemc.cluster.plugin.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AllArgsConstructor;
import net.bytemc.cluster.api.Cluster;

@AllArgsConstructor
public final class VelocityListener {

    private ProxyServer proxyServer;

    @Subscribe
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
    public void handleKick(KickedFromServerEvent event) {
        //todo
    }
}
