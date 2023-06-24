package net.bytemc.cluster.api.properties;

import net.bytemc.cluster.api.misc.async.AsyncTask;

public interface PropertyHolder {

    void removeProperty(String id);

    <T> T setProperty(String id, T value);

    <T> AsyncTask<Property<T>> requestPropertyAsync(String id);

    <T> Property<T> requestProperty(String id);

}
