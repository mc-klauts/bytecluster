package net.bytemc.bytecluster.wrapper.property;

import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.misc.GsonHelper;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.packets.properties.PropertyDeletePacket;
import net.bytemc.cluster.api.network.packets.properties.PropertyRequestSharePacket;
import net.bytemc.cluster.api.network.packets.properties.PropertySetPacket;
import net.bytemc.cluster.api.network.packets.properties.PropertySharePacket;
import net.bytemc.cluster.api.properties.GlobalPropertyHandler;
import net.bytemc.cluster.api.properties.LocalProperty;
import net.bytemc.cluster.api.properties.Property;

public final class WrapperGlobalPropertyHandler implements GlobalPropertyHandler {

    @Override
    public void removeProperty(String id) {
        Wrapper.getInstance().sendPacket(new PropertyDeletePacket(id));
    }

    @Override
    public <T> T setProperty(String id, T value) {
        Wrapper.getInstance().sendPacket(new PropertySetPacket(id, value.getClass().getName(), GsonHelper.SENDABLE_GSON.toJson(value)));
        return value;
    }

    @Override
    public <T> AsyncTask<Property<T>> requestPropertyAsync(String id) {
        var task = new AsyncTask<Property<T>>();
        Wrapper.getInstance().sendQueryPacket(new PropertyRequestSharePacket(id), PropertySharePacket.class, response -> {
            task.complete(new LocalProperty<>(response.getPropertyAsString()));
        });
        return task;
    }

    @Override
    public <T> Property<T> requestProperty(String id) {
        return (Property<T>) requestPropertyAsync(id).getSync(null);
    }
}
