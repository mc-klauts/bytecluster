package net.bytemc.cluster.node.event;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.*;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.misc.UnsafeAccess;
import net.bytemc.cluster.api.misc.concurrent.ListHelper;
import net.bytemc.cluster.api.misc.concurrent.Tuple3;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.modules.CloudModule;
import net.bytemc.cluster.node.modules.LoadedModule;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;
import net.bytemc.cluster.node.services.LocalCloudService;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CloudEventHandlerImpl extends AbstractEventHandler {

    private final Map<CloudModule, Map<Class<? extends CloudEvent>, List<CloudEventElement>>> cloudModuleEventMethods = new HashMap<>();

    private final Map<Class<? extends CloudEvent>, List<CloudService>> subscribedServices = new HashMap<>();

    // for special things on node
    public CloudEventHandlerImpl() {
        Cluster.getInstance().getPacketPool().registerListener(SubscribeEventPacket.class, (channel, subscribeEventPacket) -> this.subscribedServices.put(subscribeEventPacket.getEventClass(),
                ListHelper.addElementInList(this.subscribedServices.getOrDefault(
                                subscribeEventPacket.getEventClass(),
                                new ArrayList<>()),
                        ((CloudServiceProviderImpl) Cluster.getInstance().getServiceProvider()).getServiceChannels().get(channel))));
    }

    @Override
    public <T> void registerListener(@NotNull Object event) {

        CloudModule module = null;

        for (var loadedModule : Node.getInstance().getModuleHandler().getLoadedModules()) {
            if (UnsafeAccess.isInJar(event.getClass(), loadedModule.getFile())) {
                module = loadedModule.getModule();
                break;
            }
        }

        if (module == null) {
            super.registerListener(event);
            return;
        }
        var cloudEvents = cloudModuleEventMethods.getOrDefault(module, new HashMap<>());
        for (var events : findEvents(event)) {
            cloudEvents.put(events.left(), ListHelper.addElementInList(cloudEvents.getOrDefault(events.getClass(), new ArrayList<>()), new CloudEventElement(events.right(), events.middle())));
        }
        cloudModuleEventMethods.put(module, cloudEvents);
    }

    public void unregisterCloudModuleListener(CloudModule module) {
        this.cloudModuleEventMethods.remove(module);
    }

    @Override
    public void call(@NotNull Object event) {
        super.call(event);

        for (Map<Class<? extends CloudEvent>, List<CloudEventElement>> value : this.cloudModuleEventMethods.values()) {
            if(value.containsKey(event.getClass())) {
                for (CloudEventElement element : value.get(event.getClass())) {
                    element.accept(event);
                }
            }
        }

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

    public void removeCloudService(CloudService cloudService) {
        for (Class<? extends CloudEvent> packet : subscribedServices.keySet()) {
            List<CloudService> services = subscribedServices.get(packet);
            services.remove(cloudService);
            this.subscribedServices.put(packet, services);
        }
    }
}
