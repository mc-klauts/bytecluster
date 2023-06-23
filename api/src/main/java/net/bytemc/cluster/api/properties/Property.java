package net.bytemc.cluster.api.properties;

public interface Property<T> {

    T getValue();

    String getPropertyAsString();

}
