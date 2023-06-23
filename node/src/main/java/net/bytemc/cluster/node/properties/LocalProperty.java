package net.bytemc.cluster.node.properties;

import lombok.Getter;
import net.bytemc.cluster.api.misc.GsonHelper;
import net.bytemc.cluster.api.properties.Property;

@Getter
public final class LocalProperty<T> implements Property<T> {

    private final String propertyAsString;
    private final T value;

    public LocalProperty(T value) {
        this.value = value;
        this.propertyAsString = GsonHelper.SENDABLE_GSON.toJson(value);
    }
}
