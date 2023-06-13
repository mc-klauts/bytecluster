package net.bytemc.bytecluster.wrapper.listener;

import net.bytemc.cluster.api.event.SubscribeEvent;
import net.bytemc.cluster.api.event.services.CloudServiceConnectEvent;
import net.bytemc.cluster.api.event.services.CloudServiceShutdownEvent;

public class ProxyServerInfoListener {

    @SubscribeEvent
    public void handleServiceConnect(CloudServiceConnectEvent event) {

    }

    @SubscribeEvent
    public void handleServiceDisconnect(CloudServiceShutdownEvent event) {

    }

}
