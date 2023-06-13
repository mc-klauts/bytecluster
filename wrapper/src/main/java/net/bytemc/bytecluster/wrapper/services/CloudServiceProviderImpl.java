package net.bytemc.bytecluster.wrapper.services;

import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceRequest;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceResponse;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceFactory;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.api.service.CloudServiceState;
import net.bytemc.cluster.api.service.filter.CloudServiceFilter;

import java.util.Collection;
import java.util.Optional;

public final class CloudServiceProviderImpl implements CloudServiceProvider {

    @Override
    public TaskFuture<Collection<CloudService>> findServicesAsync() {
        //todo
        return null;
    }

    @Override
    public Collection<CloudService> findServices() {
        //todo
        return null;
    }

    @Override
    public Collection<CloudService> findServices(CloudServiceFilter filter) {
        //todo
        return null;
    }

    @Override
    public TaskFuture<CloudService> findServiceAsync(String name) {
        var tasks = new TaskFuture<CloudService>();
        Wrapper.getInstance().sendQueryPacket(new SingletonServiceRequest(name), SingletonServiceResponse.class, (packet) -> tasks.complete(packet.getCloudService()));
        return tasks;
    }

    @Override
    public CloudService findService(String name) {
        //todo
        return null;
    }

    @Override
    public TaskFuture<Collection<CloudService>> findServicesAsync(String group) {
        //todo
        return null;
    }

    @Override
    public Collection<CloudService> findServices(String group) {
        //todo
        return null;
    }

    @Override
    public Optional<CloudService> findFallback() {
        //todo
        return Optional.empty();
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
