package net.bytemc.cluster.node.services;

import lombok.Getter;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.service.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CloudServiceProviderImpl implements CloudServiceProvider {

    @Getter
    private final CloudServiceFactory factory = new CloudServiceFactoryImpl();
    private final CloudServiceFactoryQueue queue = new CloudServiceFactoryQueue(this);

    private final Map<String, CloudService> services = new HashMap<>();

    public CloudServiceProviderImpl(CloudServiceGroupProvider groupProvider) {
        for (var group : groupProvider.findGroups()) {
            if(group.getMinOnlineCount() <= 0) {
                continue;
            }
            for (int i = 0; i < group.getMinOnlineCount(); i++) {
                this.queue.addTask(group);
            }
        }
    }

    @Override
    public TaskFuture<Collection<CloudService>> findServicesAsync() {
        return null;
    }

    @Override
    public Collection<CloudService> findServices() {
        return this.services.values();
    }

    @Override
    public TaskFuture<CloudService> findServiceAsync(String name) {
        return null;
    }

    @Override
    public CloudService findService(String name) {
        return findServices().stream().filter(it -> it.getName().equals(name)).findFirst().get();
    }

    @Override
    public TaskFuture<Collection<CloudService>> findServicesAsync(String group) {
        return null;
    }

    @Override
    public Collection<CloudService> findServices(String group) {
        return findServices().stream().filter(it -> it.getGroupName().equalsIgnoreCase(group)).toList();
    }

    @Override
    public Optional<CloudService> findFallback() {
        return Optional.empty();
    }

    public void queue() {
        this.queue.start();
    }


}
