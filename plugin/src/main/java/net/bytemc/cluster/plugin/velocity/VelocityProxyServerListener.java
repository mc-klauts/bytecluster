package net.bytemc.cluster.plugin.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.AllArgsConstructor;
import net.bytemc.cluster.api.event.SubscribeEvent;
import net.bytemc.cluster.api.event.services.CloudServiceConnectEvent;
import net.bytemc.cluster.api.event.services.CloudServiceShutdownEvent;

import java.net.InetSocketAddress;

@AllArgsConstructor
public final class VelocityProxyServerListener {

    private final ProxyServer proxyServer;

    @SubscribeEvent
    public void handle(CloudServiceConnectEvent event) {
        System.out.println("polo123");

        event.getService().getGroupAsync().whenComplete((group, throwable) -> {
            if(!group.getGroupType().isProxy()) {
                proxyServer.registerServer(new ServerInfo(event.getService().getName(), new InetSocketAddress(event.getService().getHostname(), event.getService().getPort())));
            }
        });
    }

    @SubscribeEvent
    public void handle(CloudServiceShutdownEvent event) {
        System.out.println("polo");
        proxyServer.getServer(event.getCloudService().getName()).ifPresent(info -> {
            proxyServer.unregisterServer(info.getServerInfo());
        });
    }
}
