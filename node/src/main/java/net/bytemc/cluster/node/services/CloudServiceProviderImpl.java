package net.bytemc.cluster.node.services;

import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceProvider;

import java.util.Collection;
import java.util.Optional;

public final class CloudServiceProviderImpl implements CloudServiceProvider {

    @Override
    public TaskFuture<Collection<CloudService>> findServicesAsync() {
        return null;
    }

    @Override
    public Collection<CloudService> findServices() {
        return null;
    }

    @Override
    public TaskFuture<CloudService> findServiceAsync(String name) {
        return null;
    }

    @Override
    public CloudService findService(String name) {
        return null;
    }

    @Override
    public TaskFuture<Collection<CloudService>> findServicesAsync(String group) {
        return null;
    }

    @Override
    public Collection<CloudService> findServices(String group) {
        return null;
    }

    @Override
    public Optional<CloudService> findFallback() {
        return Optional.empty();
    }
}
