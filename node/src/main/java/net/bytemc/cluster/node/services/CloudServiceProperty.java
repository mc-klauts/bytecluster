package net.bytemc.cluster.node.services;

import lombok.Getter;
import net.bytemc.cluster.api.misc.GsonHelper;
import net.bytemc.cluster.api.properties.Property;

@Getter
public final class CloudServiceProperty<T> implements Property<T> {

    private String id;
    private String propertyAsString;

    public CloudServiceProperty(String id, String gson) {
        this.id = id;
        this.propertyAsString = gson;
    }

    @Override
    public T getValue(Class<T> clazz) {
        return GsonHelper.SENDABLE_GSON.fromJson(propertyAsString, clazz);
    }
}
