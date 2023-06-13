package net.bytemc.cluster.plugin.bootstrap;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id = "bytemc-proxy", name = "bytemc-Proxy", version = "1.0.0")
public final class VelocityPlatformBootstrap {


    @Inject
    private ProxyServer proxyServer;

    @Subscribe
    public void handleProxyInitializeEvent(final ProxyInitializeEvent event) {

    }


}
