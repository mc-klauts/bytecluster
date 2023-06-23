package net.bytemc.bytecluster.wrapper.property;

import lombok.Getter;
import net.bytemc.cluster.api.misc.GsonHelper;
import net.bytemc.cluster.api.properties.Property;

@Getter
public class WrapperProperty<T> implements Property<T> {

    private String id;
    private String propertyAsString;
    private T value;

    public WrapperProperty(String id, Class<?> clazz, String gson) {
        this.id = id;
        this.propertyAsString = gson;
        this.value = (T) GsonHelper.SENDABLE_GSON.fromJson(gson, clazz);
    }
}
