package net.bytemc.cluster.node.properties;

import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.properties.GlobalPropertyHandler;
import net.bytemc.cluster.api.properties.LocalProperty;
import net.bytemc.cluster.api.properties.Property;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GlobalPropertyHandlerImpl implements GlobalPropertyHandler {

    private Map<String, Property<?>> properties = new ConcurrentHashMap<>();

    @Override
    public void removeProperty(String id) {
        this.properties.remove(id);
    }

    @Override
    public <T> T setProperty(String id, T value) {
        LocalProperty<T> property = new LocalProperty<>(value);
        properties.put(id, property);
        return property.getValue();
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
