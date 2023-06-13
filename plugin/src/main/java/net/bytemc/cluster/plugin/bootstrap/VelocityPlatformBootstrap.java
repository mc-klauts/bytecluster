package net.bytemc.cluster.plugin.bootstrap;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.filter.CloudServiceFilter;
import net.bytemc.cluster.plugin.velocity.VelocityProxyServerListener;

@Plugin(id = "bytemc-proxy", name = "bytemc-Proxy", version = "1.0.0")
public final class VelocityPlatformBootstrap {


    @Inject
    private ProxyServer proxyServer;

    @Subscribe
    public void handleProxyInitializeEvent(final ProxyInitializeEvent event) {

        Cluster.getInstance().getEventHandler().registerListener(new VelocityProxyServerListener(this.proxyServer));

        // unregister all default config configuration
        this.proxyServer.getAllServers().forEach(server -> this.proxyServer.unregisterServer(server.getServerInfo()));

        //register all default fallbacks
        for (var service : Cluster.getInstance().getServiceProvider().findServices(CloudServiceFilter.NON_PROXIES)) {
            //todo
        }
    }
}
