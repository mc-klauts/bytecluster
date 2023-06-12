package net.bytemc.cluster.api.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public record CloudEventElement(Object parent, Method eventMethode) {

    public void accept(Object event) {
        try {
            eventMethode.invoke(parent, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
