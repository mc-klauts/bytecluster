package net.bytemc.cluster.node.services;

import lombok.Getter;
import net.bytemc.cluster.api.misc.GsonHelper;
import net.bytemc.cluster.api.properties.Property;

@Getter
public final class CloudServiceProperty<T> implements Property<T> {

    private String id;
    private String propertyAsString;
    private T value;

    public CloudServiceProperty(String id, Class<?> clazz, String gson) {
        this.id = id;
        this.propertyAsString = gson;
        this.value = (T) GsonHelper.SENDABLE_GSON.fromJson(gson, clazz);
    }
}
