package net.bytemc.cluster.node.properties;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.misc.GsonHelper;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.packets.properties.PropertyDeletePacket;
import net.bytemc.cluster.api.network.packets.properties.PropertySetPacket;
import net.bytemc.cluster.api.properties.GlobalPropertyHandler;
import net.bytemc.cluster.api.properties.LocalProperty;
import net.bytemc.cluster.api.properties.Property;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GlobalPropertyHandlerImpl implements GlobalPropertyHandler {

    private Map<String, Property<?>> properties = new ConcurrentHashMap<>();

    public GlobalPropertyHandlerImpl() {
        Cluster.getInstance().getPacketPool().registerListener(PropertySetPacket.class, (channel, propertySetPacket) -> {
            setProperty(propertySetPacket.getId(), propertySetPacket.getPropertyAsString());
        });
        Cluster.getInstance().getPacketPool().registerListener(PropertyDeletePacket.class, (channel, propertyDeletePacket) -> {
            removeProperty(propertyDeletePacket.getId());
        });
    }

    @Override
    public void removeProperty(String id) {
        this.properties.remove(id);
    }

    public void setProperty(String id, String value) {
        properties.put(id, new LocalProperty<>(value));
    }

    @Override
    public <T> T setProperty(String id, T value) {
        var property = new LocalProperty<>(value);
        properties.put(id, property);
        return property.getValue((Class<T>) value.getClass());
    }

    @Override
    public <T> AsyncTask<Property<T>> requestPropertyAsync(String id) {
        return AsyncTask.directly(requestProperty(id));
    }

    @Override
    public <T> Property<T> requestProperty(String id) {
        return (Property<T>) properties.get(id);
    }
}
