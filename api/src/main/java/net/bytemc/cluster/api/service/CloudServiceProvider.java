package net.bytemc.cluster.api.service;

import net.bytemc.cluster.api.misc.TaskFuture;

import java.util.Collection;
import java.util.Optional;

public interface CloudServiceProvider {

    TaskFuture<Collection<CloudService>> findServicesAsync();

    Collection<CloudService> findServices();

    TaskFuture<CloudService> findServiceAsync(String name);

    CloudService findService(String name);

    TaskFuture<Collection<CloudService>> findServicesAsync(String group);

    Collection<CloudService> findServices(String group);

    Optional<CloudService> findFallback();

}
