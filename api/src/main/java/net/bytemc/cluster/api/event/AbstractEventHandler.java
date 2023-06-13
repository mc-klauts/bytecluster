package net.bytemc.cluster.api.event;

import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.misc.concurrent.ListHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEventHandler implements EventHandler {

    private final Map<Class<? extends CloudEvent>, List<CloudEventElement>> cloudEventMethods = new HashMap<>();

    @Override
    public <T> void registerListener(@NotNull Object event) {
        for (var method : event.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubscribeEvent.class)) {
                var parameters = method.getParameters();
                if (parameters.length != 1) {
                    Logger.warn("Event method " + method.getName() + " has not exactly one parameter (" + event.getClass().getSimpleName() + ")");
                    return;
                }
                this.registerEvent((Class<? extends CloudEvent>) parameters[0].getType(), method, event);
            }
        }
    }

    public void registerEvent(Class<? extends CloudEvent> parameter, Method method, Object event) {
        cloudEventMethods.put(parameter, ListHelper.addElementInList(cloudEventMethods.getOrDefault(parameter, new ArrayList<>()), new CloudEventElement(event, method)));
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
