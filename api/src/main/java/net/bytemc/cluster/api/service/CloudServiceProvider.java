package net.bytemc.cluster.api.service;

import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.filter.CloudServiceFilter;

import java.util.Collection;
import java.util.Optional;

public interface CloudServiceProvider {

    TaskFuture<Collection<CloudService>> findServicesAsync();

    Collection<CloudService> findServices();

    Collection<CloudService> findServices(CloudServiceFilter filter);

    TaskFuture<CloudService> findServiceAsync(String name);

    CloudService findService(String name);

    TaskFuture<Collection<CloudService>> findServicesAsync(String group);

    Collection<CloudService> findServices(String group);

    Optional<CloudService> findFallback();

    CloudServiceFactory getFactory();

    CloudService getCloudServiceByBuffer(PacketBuffer buffer);

}
