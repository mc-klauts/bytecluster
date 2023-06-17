package net.bytemc.cluster.node;

import net.bytemc.cluster.api.event.SubscribeEvent;
import net.bytemc.cluster.api.player.events.CloudPlayerConnectEvent;

public class TestListener {

    @SubscribeEvent
    public void hanlde(CloudPlayerConnectEvent event) {
        event.getCloudPlayer().sendTablist("Polo ist fett", "foort test");
    }

}
