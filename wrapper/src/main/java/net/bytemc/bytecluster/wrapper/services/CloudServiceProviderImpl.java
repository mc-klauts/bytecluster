package net.bytemc.bytecluster.wrapper.services;

import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceRequest;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceResponse;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceFactory;
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
        TaskFuture<CloudService> tasks = new TaskFuture<>();

        Wrapper.getInstance().sendQueryPacket(new SingletonServiceRequest(), SingletonServiceResponse.class, (packet) -> {
            System.out.println("found");
        });
        return tasks;
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

    @Override
    public CloudServiceFactory getFactory() {
        return null;
    }

    @Override
    public CloudService getCloudServiceByBuffer(PacketBuffer buffer) {
        return null;
    }
}
