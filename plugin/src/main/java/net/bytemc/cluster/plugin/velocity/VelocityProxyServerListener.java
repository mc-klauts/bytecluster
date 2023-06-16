package net.bytemc.cluster.plugin.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.AllArgsConstructor;
import net.bytemc.cluster.api.event.SubscribeEvent;
import net.bytemc.cluster.api.event.services.CloudServiceConnectEvent;
import net.bytemc.cluster.api.event.services.CloudServiceShutdownEvent;

import java.net.InetSocketAddress;
import java.util.Optional;

@AllArgsConstructor
public final class VelocityProxyServerListener {

    private ProxyServer proxyServer;

    @SubscribeEvent
    public void handleCloudServiceConnect(CloudServiceConnectEvent event) {
        event.getService().getGroupAsync().onComplete(cloudServiceGroup -> {
            if (!cloudServiceGroup.getGroupType().isProxy()) {
                this.proxyServer.registerServer(new ServerInfo(event.getService().getName(), new InetSocketAddress(event.getService().getHostname(), event.getService().getPort())));
            }
        });
    }

    @SubscribeEvent
    public void handleCLoudServiceDisconnect(CloudServiceShutdownEvent event) {
        Optional<RegisteredServer> server = this.proxyServer.getServer(event.getCloudService().getName());
        if (server.isPresent()) {
            this.proxyServer.unregisterServer(server.get().getServerInfo());
        }
    }
}