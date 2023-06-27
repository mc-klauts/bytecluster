package net.bytemc.bytecluster.wrapper.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.misc.GsonHelper;
import net.bytemc.cluster.api.properties.Property;

@Getter
@AllArgsConstructor
public class WrapperProperty<T> implements Property<T> {

    private String id;
    private String clazz;
    private String propertyAsString;

    @Override
    public T getValue(ClassLoader classLoader) {
        try {
            return (T) GsonHelper.SENDABLE_GSON.fromJson(propertyAsString, Class.forName(clazz, true, classLoader));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public T getValue() {
        try {
            return (T) GsonHelper.SENDABLE_GSON.fromJson(propertyAsString, Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
