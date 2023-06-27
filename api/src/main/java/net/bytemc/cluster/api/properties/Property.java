package net.bytemc.cluster.api.properties;

public interface Property<T> {

    T getValue(ClassLoader classLoader);

    T getValue();

    String getPropertyAsString();

}
