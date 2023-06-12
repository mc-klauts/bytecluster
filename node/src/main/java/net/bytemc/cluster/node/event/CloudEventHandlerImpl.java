package net.bytemc.cluster.node.event;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.*;
import net.bytemc.cluster.api.misc.ListHelper;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;
import net.bytemc.cluster.node.services.LocalCloudService;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CloudEventHandlerImpl extends AbstractEventHandler {

    private Map<Class<? extends CloudEvent>, List<CloudService>> subscribedServices = new HashMap<>();

    // for special things on node
    public CloudEventHandlerImpl() {
        Cluster.getInstance().getPacketPool().registerListener(SubscribeEventPacket.class, (channel, subscribeEventPacket) -> this.subscribedServices.put(subscribeEventPacket.getEventClass(),
                ListHelper.addElementInList(this.subscribedServices.getOrDefault(
                                subscribeEventPacket.getEventClass(),
                                new ArrayList<>()),
                        ((CloudServiceProviderImpl) Cluster.getInstance().getServiceProvider()).getServiceChannels().get(channel))));
    }

    @Override
    public void call(@NotNull Object event) {
        super.call(event);

        if (event instanceof AbstractCommunicatableEvent communicatableEvent) {

            if (!subscribedServices.containsKey(event.getClass())) {
                return;
            }

            for (var cloudService : subscribedServices.get(event.getClass())) {
                if (cloudService instanceof LocalCloudService localCloudService) {
                    localCloudService.sendPacket(new CallEventPacket(communicatableEvent));
                }
            }
        }
    }
}
