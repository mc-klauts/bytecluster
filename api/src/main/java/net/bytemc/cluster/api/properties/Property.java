package net.bytemc.cluster.api.properties;

public interface Property<T> {

    T getValue(Class<T> clazz);

    String getPropertyAsString();

}
