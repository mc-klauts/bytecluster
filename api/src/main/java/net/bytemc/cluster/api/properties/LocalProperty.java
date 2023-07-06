package net.bytemc.cluster.api.properties;

import lombok.Getter;
import net.bytemc.cluster.api.misc.GsonHelper;

@Getter
public class LocalProperty<T> implements Property<T> {

    private final String propertyAsString;

    public LocalProperty(T value) {
        this.propertyAsString = GsonHelper.SENDABLE_GSON.toJson(value);
    }

    public LocalProperty(String value) {
        this.propertyAsString = value;
    }

    @Override
    public T getValue(Class<T> clazz) {
        return GsonHelper.SENDABLE_GSON.fromJson(propertyAsString, clazz);
    }
}
