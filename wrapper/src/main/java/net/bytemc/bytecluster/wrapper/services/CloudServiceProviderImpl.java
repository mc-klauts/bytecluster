package net.bytemc.bytecluster.wrapper.services;

import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.network.packets.services.*;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceFactory;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.api.service.CloudServiceState;
import net.bytemc.cluster.api.service.filter.CloudServiceFilter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public final class CloudServiceProviderImpl implements CloudServiceProvider {

    @Override
    public Collection<CloudService> findServices() {
        return findServicesAsync().getSync(null);
    }

    @Override
    public AsyncTask<Collection<CloudService>> findServicesAsync() {
        var tasks = new AsyncTask<Collection<CloudService>>();
        Wrapper.getInstance().sendQueryPacket(new CollectionServiceRequest(CloudServiceFilter.ALL), CollectionServiceResponse.class, (packet) -> {
            tasks.complete(packet.getServices());
        });
        return tasks;
    }

    @Override
    public Collection<CloudService> findServices(CloudServiceFilter filter) {
        return findServicesAsync(filter).getSync(null);
    }

    @Override
    @NotNull
    public AsyncTask<Collection<CloudService>> findServicesAsync(CloudServiceFilter filter) {
        var tasks = new AsyncTask<Collection<CloudService>>();
        Wrapper.getInstance().sendQueryPacket(new CollectionServiceRequest(filter), CollectionServiceResponse.class, (packet) -> tasks.complete(packet.getServices()));
        return tasks;
    }


    @Override
    public CloudService findService(String name) {
        return findServiceAsync(name).getSync(null);
    }

    @Override
    public @NotNull AsyncTask<CloudService> findServiceAsync(String name) {
        var tasks = new AsyncTask<CloudService>();
        Wrapper.getInstance().sendQueryPacket(new SingletonServiceRequest(name), SingletonServiceResponse.class, (packet) -> tasks.complete(packet.getCloudService()));
        return tasks;
    }

    @Contract(pure = true)
    @Override
    public @Nullable Collection<CloudService> findServices(String group) {
        //todo
        return null;
    }

    @Override
    public AsyncTask<Collection<CloudService>> findServicesAsync(String group) {
        return null;
    }

    @Override
    public Optional<String> findFallbackId() {
        return this.findFallbackIdAsync().getSync(Optional.empty());
    }

    @Override
    public @NotNull AsyncTask<Optional<String>> findFallbackIdAsync() {
        var tasks = new AsyncTask<Optional<String>>();
        Wrapper.getInstance().sendQueryPacket(new FindFallbackServiceRequest(), FindFallbackServiceResponse.class, (packet) -> tasks.complete(packet.getFallbackId()));
        return tasks;
    }

    @Override
    public Optional<CloudService> findFallback() {
        //todo
        return null;
    }

    @Contract(pure = true)
    @Override
    public @Nullable AsyncTask<Optional<CloudService>> findFallbackAsync() {
        return null;
    }

    @Override
    public CloudServiceFactory getFactory() {
        //todo
        return null;
    }

    @Override
    public CloudService getCloudServiceByBuffer(PacketBuffer buffer) {

        int id = buffer.readInt();
        var hostname = buffer.readString();
        var groupName = buffer.readString();
        var motd = buffer.readString();
        var maxPlayers = buffer.readInt();
        var port = buffer.readInt();

        //todo onlinestate
        return new WrapperCloudService(hostname, groupName, motd, port, id, maxPlayers, CloudServiceState.ONLINE);
    }
}
