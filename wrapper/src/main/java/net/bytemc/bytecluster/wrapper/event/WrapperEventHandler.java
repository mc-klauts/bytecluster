package net.bytemc.bytecluster.wrapper.event;

import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.*;

import java.lang.reflect.Method;

public final class WrapperEventHandler extends AbstractEventHandler {

    public WrapperEventHandler() {
        var pool = Cluster.getInstance().getPacketPool();

        //call external events form node
        pool.registerListener(CallEventPacket.class, (channel, packet) -> call(packet.getEvent()));
    }

    @Override
    public void registerEvent(Class<? extends CloudEvent> parameter, Method method, Object event) {
        super.registerEvent(parameter, method, event);

        if (parameter.getSuperclass() != null && parameter.getSuperclass().equals(AbstractCommunicatableEvent.class)) {
            // call to node that subscribe this events
            Wrapper.getInstance().sendPacket(new SubscribeEventPacket((Class<? extends AbstractCommunicatableEvent>) parameter));
        }
    }
}
