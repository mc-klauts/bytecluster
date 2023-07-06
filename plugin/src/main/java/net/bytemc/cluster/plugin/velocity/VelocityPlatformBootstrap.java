package net.bytemc.cluster.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.command.CommandRepository;
import net.bytemc.cluster.api.service.filter.CloudServiceFilter;
import net.bytemc.cluster.plugin.velocity.command.VelocityCommandSupport;

import java.net.InetSocketAddress;

@Plugin(id = "bytecluster-plugin", name = "bytecluster-plugin", version = "1.0.0", authors = {"bytemc"})
public final class VelocityPlatformBootstrap {

    @Inject
    private ProxyServer proxyServer;

    @Subscribe
    public void handleProxyInitializeEvent(final ProxyInitializeEvent event) {

        Cluster.getInstance().getEventHandler().registerListener(new VelocityProxyServerListener(this.proxyServer));

        this.proxyServer.getEventManager().register(this, new VelocityListener(this.proxyServer));

        new VelocityPacketListener(this.proxyServer);

        // unregister all default config configuration
        this.proxyServer.getAllServers().forEach(server -> this.proxyServer.unregisterServer(server.getServerInfo()));

        //register all default fallbacks
        for (var service : Cluster.getInstance().getServiceProvider().findServices(CloudServiceFilter.NON_PROXIES)) {
            this.proxyServer.registerServer(new ServerInfo(service.getName(), new InetSocketAddress(service.getHostname(), service.getPort())));
        }

        // register commands
        final VelocityCommandSupport commandSupport = new VelocityCommandSupport(this.proxyServer);
        commandSupport.registerAllCommandsOnVelocity();
    }
}
