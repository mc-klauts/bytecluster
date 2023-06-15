package net.bytemc.cluster.plugin.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AllArgsConstructor;
import net.bytemc.cluster.api.event.SubscribeEvent;
import net.bytemc.cluster.api.event.services.CloudServiceConnectEvent;

@AllArgsConstructor
public final class VelocityProxyServerListener {

    private ProxyServer proxyServer;

    @SubscribeEvent
    public void handleCloudServiceConnect(CloudServiceConnectEvent event) {
        //todo
        /*
        Cluster.getInstance().getServiceGroupProvider().getGroupAsync(event.getService().group()).whenComplete((serviceGroup, throwable) -> {
            if (!serviceGroup.getVersion().isProxy()) {
                this.proxyServer.registerServer(new ServerInfo(event.getService().getName(), new InetSocketAddress("127.0.0.1", event.getService().getPort())));
            }
        });

         */
    }
}
