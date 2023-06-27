package net.bytemc.cluster.api.properties;

import lombok.Getter;
import net.bytemc.cluster.api.misc.GsonHelper;

@Getter
public class LocalProperty<T> implements Property<T> {

    private final String propertyAsString;
    private final T value;

    public LocalProperty(T value) {
        this.value = value;
        this.propertyAsString = GsonHelper.SENDABLE_GSON.toJson(value);
    }

    @Override
    public T getValue(ClassLoader classLoader) {
        return value;
    }
}
