package net.bytemc.cluster.api.service;

import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.filter.CloudServiceFilter;

import java.util.Collection;
import java.util.Optional;

public interface CloudServiceProvider {

    Collection<CloudService> findServices();

    AsyncTask<Collection<CloudService>> findServicesAsync();

    Collection<CloudService> findServices(CloudServiceFilter filter);

    AsyncTask<Collection<CloudService>> findServicesAsync(CloudServiceFilter filter);

    CloudService findService(String name);

    AsyncTask<CloudService> findServiceAsync(String name);

    Collection<CloudService> findServices(String group);

    AsyncTask<Collection<CloudService>> findServicesAsync(String group);

    Optional<CloudService> findFallback();

    AsyncTask<Optional<CloudService> >findFallbackAsync();

    CloudServiceFactory getFactory();

    CloudService getCloudServiceByBuffer(PacketBuffer buffer);

}
