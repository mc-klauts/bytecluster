package net.bytemc.cluster.node.services;

import lombok.Getter;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.service.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public final class CloudServiceProviderImpl implements CloudServiceProvider {

    private final CloudServiceFactory factory = new CloudServiceFactoryImpl();
    private final CloudServiceFactoryQueue queue = new CloudServiceFactoryQueue(this);

    private final Map<String, CloudService> services = new HashMap<>();

    public CloudServiceProviderImpl(@NotNull CloudServiceGroupProvider groupProvider) {
        for (var group : groupProvider.findGroups()) {
            if (group.getMinOnlineCount() <= 0) {
                continue;
            }
            this.queue.addTask(group, group.getMinOnlineCount());
        }
    }

    @Contract(pure = true)
    @Override
    public @Nullable TaskFuture<Collection<CloudService>> findServicesAsync() {
        return null;
    }

    @Contract(pure = true)
    @Override
    public @NotNull Collection<CloudService> findServices() {
        return this.services.values();
    }

    @Contract(pure = true)
    @Override
    public @Nullable TaskFuture<CloudService> findServiceAsync(String name) {
        return null;
    }

    @Override
    public @NotNull CloudService findService(String name) {
        return findServices().stream().filter(it -> it.getName().equals(name)).findFirst().get();
    }

    @Contract(pure = true)
    @Override
    public @Nullable TaskFuture<Collection<CloudService>> findServicesAsync(String group) {
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

    public void addService(CloudService service) {
        this.services.put(service.getName(), service);
    }

}
