package net.bytemc.bytecluster.wrapper.event;

import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.event.AbstractCommunicatableEvent;
import net.bytemc.cluster.api.event.AbstractEventHandler;
import net.bytemc.cluster.api.event.CloudEvent;
import net.bytemc.cluster.api.event.SubscribeEventPacket;

import java.lang.reflect.Method;

public final class WrapperEventHandler extends AbstractEventHandler {

    @Override
    public void registerEvent(Class<? extends CloudEvent> parameter, Method method, Object event) {
        super.registerEvent(parameter, method, event);

        if (parameter.getSuperclass() != null && parameter.getSuperclass().equals(AbstractCommunicatableEvent.class)) {
            // call to node that subscribe this events
            Wrapper.getInstance().sendPacket(new SubscribeEventPacket(parameter));
        }
    }
}
