package net.bytemc.bytecluster.wrapper.services;

import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.network.packets.services.CollectionServiceRequest;
import net.bytemc.cluster.api.network.packets.services.CollectionServiceResponse;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceRequest;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceResponse;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceFactory;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.api.service.CloudServiceState;
import net.bytemc.cluster.api.service.filter.CloudServiceFilter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public final class CloudServiceProviderImpl implements CloudServiceProvider {

    @Override
    public Collection<CloudService> findServices() {
        return findServicesAsync().getSync(null);
    }

    @Override
    public AsyncTask<Collection<CloudService>> findServicesAsync() {
        var tasks = new AsyncTask<Collection<CloudService>>();
        Wrapper.getInstance().sendQueryPacket(new CollectionServiceRequest(CollectionServiceRequest.Filter.ALL), CollectionServiceResponse.class, (packet) -> {
            System.out.println("bin da");
            tasks.complete(packet.getServices());
        });
        return tasks;
    }

    @Override
    public Collection<CloudService> findServices(CloudServiceFilter filter) {
        return findServicesAsync(filter).getSync(null);
    }

    @Override @NotNull
    public AsyncTask<Collection<CloudService>> findServicesAsync(CloudServiceFilter filter) {
        //todo
        return null;
    }


    @Override
    public CloudService findService(String name) {
        return findServiceAsync(name).getSync(null);
    }

    @Override
    public AsyncTask<CloudService> findServiceAsync(String name) {
        var tasks = new AsyncTask<CloudService>();
        Wrapper.getInstance().sendQueryPacket(new SingletonServiceRequest(name), SingletonServiceResponse.class, (packet) -> tasks.complete(packet.getCloudService()));
        return tasks;
    }

    @Override
    public Collection<CloudService> findServices(String group) {
        //todo
        return null;
    }

    @Override
    public AsyncTask<Collection<CloudService>> findServicesAsync(String group) {
        return null;
    }

    @Override
    public Optional<CloudService> findFallback() {
        //todo
        return null;
    }

    @Override
    public AsyncTask<Optional<CloudService>> findFallbackAsync() {
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
