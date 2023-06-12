package net.bytemc.cluster.node.event;

import net.bytemc.cluster.api.event.CloudEvent;
import net.bytemc.cluster.api.event.CloudEventElement;
import net.bytemc.cluster.api.event.EventHandler;
import net.bytemc.cluster.api.event.SubscribeEvent;
import net.bytemc.cluster.api.event.services.CloudServiceConnectEvent;
import net.bytemc.cluster.node.logger.Logger;
import net.bytemc.cluster.node.misc.ListHelper;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class CloudEventHandlerImpl implements EventHandler {

    private Map<Class<? extends CloudEvent>, List<CloudEventElement>> cloudEventMethods = new HashMap<>();

    public CloudEventHandlerImpl() {
        registerListener(this);
    }

    @SubscribeEvent(priority = 1)
    public void onTest(CloudServiceConnectEvent event) {
        System.out.println("TestEvent");
    }

    @SubscribeEvent(priority = 0)
    public void onTest2(CloudServiceConnectEvent event) {
        System.out.println("polo");
    }


    @Override
    public <T> void registerListener(@NotNull Object event) {
        for (var method : event.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubscribeEvent.class)) {
                var parameters = method.getParameters();
                if (parameters.length != 1) {
                    Logger.warn("Event method " + method.getName() + " has not exactly one parameter (" + event.getClass().getSimpleName() + ")");
                    return;
                }
                cloudEventMethods.put((Class<? extends CloudEvent>) parameters[0].getType(),
                        ListHelper.addElementInList(cloudEventMethods.getOrDefault(parameters[0].getType(),
                                new ArrayList<>()), new CloudEventElement(event, method)));
            }
        }
    }

    @Override
    public void call(@NotNull Object event) {
        if (!cloudEventMethods.containsKey(event.getClass())) {
            return;
        }
        cloudEventMethods.get(event.getClass())
                .stream()
                .sorted((o1, o2) -> Integer.valueOf(o1.eventMethode().getDeclaredAnnotation(SubscribeEvent.class).priority())
                        .compareTo(o2.eventMethode().getDeclaredAnnotation(SubscribeEvent.class).priority()))
                .toList()
                .forEach(eventElement -> eventElement.accept(event));
    }
}
