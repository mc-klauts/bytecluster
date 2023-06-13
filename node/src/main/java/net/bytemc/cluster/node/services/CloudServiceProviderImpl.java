package net.bytemc.cluster.node.services;

import io.netty5.channel.Channel;
import lombok.Getter;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import net.bytemc.cluster.api.service.filter.CloudServiceFilter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public final class CloudServiceProviderImpl implements CloudServiceProvider {

    private final CloudServiceFactory factory = new CloudServiceFactoryImpl();
    private final CloudServiceFactoryQueue queue = new CloudServiceFactoryQueue(this);

    private final Map<String, CloudService> services = new ConcurrentHashMap<>();

    // extra separated map for faster access
    private final Map<Channel, CloudService> serviceChannels = new HashMap<>();

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
    public @NotNull Collection<CloudService> findServices() {
        return this.services.values();
    }

    @Override
    public AsyncTask<Collection<CloudService>> findServicesAsync() {
        return AsyncTask.completeWork(findServices());
    }

    @Override
    public Collection<CloudService> findServices(CloudServiceFilter filter) {
        return null;
    }

    @Override
    public AsyncTask<Collection<CloudService>> findServicesAsync(CloudServiceFilter filter) {
        return null;
    }

    @Override @Nullable
    public CloudService findService(String name) {
        return findServices().stream().filter(it -> it.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public AsyncTask<CloudService> findServiceAsync(String name) {
        return AsyncTask.completeWork(findService(name));
    }

    @Override
    public Collection<CloudService> findServices(String group) {
        return findServices().stream().filter(it -> it.getGroupName().equalsIgnoreCase(group)).toList();
    }

    @Override
    public AsyncTask<Collection<CloudService>> findServicesAsync(String group) {
        return AsyncTask.completeWork(findServices(group));
    }

    @Override
    public Optional<CloudService> findFallback() {
        return Optional.empty();
    }

    @Override
    public AsyncTask<Optional<CloudService>> findFallbackAsync() {
        return AsyncTask.completeWork(findFallback());
    }

    @Override
    public CloudService getCloudServiceByBuffer(PacketBuffer buffer) {
        //todo
        return null;
    }

    public void queue() {
        this.queue.start();
    }

    public void addService(CloudService service) {
        this.services.put(service.getName(), service);
    }

    public void removeService(String service) {
        this.services.remove(service);
    }

    public boolean isConnectionVerified(Channel channel) {
        return this.serviceChannels.containsKey(channel);
    }

    public CloudService getServiceByConnection(Channel channel) {
        return this.serviceChannels.get(channel);
    }

    public void addServiceConnection(Channel channel, CloudService service) {
        this.serviceChannels.put(channel, service);

        if (service instanceof LocalCloudService localCloudService) {
            localCloudService.setChannel(channel);
        }
    }
}
