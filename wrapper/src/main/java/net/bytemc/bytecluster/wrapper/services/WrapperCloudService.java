package net.bytemc.bytecluster.wrapper.services;

import lombok.Getter;
import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.packets.services.*;
import net.bytemc.cluster.api.properties.Property;
import net.bytemc.cluster.api.service.AbstractCloudService;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceState;

@Getter
public class WrapperCloudService extends AbstractCloudService {

    public WrapperCloudService(String hostname, String groupName, String motd, int port, int id, int maxPlayers, CloudServiceState state) {
        super(hostname, groupName, motd, port, id, maxPlayers, state);
    }

    @Override
    public int getPlayers() {
        return getPlayersAsync().getSync(null);
    }

    @Override
    public AsyncTask<Integer> getPlayersAsync() {
        var tasks = new AsyncTask<Integer>();
        Wrapper.getInstance().sendQueryPacket(new CloudServiceRequestPlayerAmountPacket(getName()), CloudServiceResponsePlayerAmountPacket.class, (packet) -> tasks.complete(packet.getAmount()));
        return tasks;
    }

    @Override
    public void executeCommand(String command) {
        Wrapper.getInstance().sendPacket(new WrapperRequestServiceCommandPacket(getName(), command));
    }

    @Override
    public void shutdown() {
        Wrapper.getInstance().sendPacket(new WrapperRequestServiceShutdownPacket(getName()));
    }


    @Override
    public void removeProperty(String id) {

    }

    @Override
    public <T> T setProperty(String id, T value) {
        return null;
    }

    @Override
    public <T> AsyncTask<Property<T>> requestPropertyAsync(String id) {
        return null;
    }

    @Override
    public <T> Property<T> requestProperty(String id) {
        return null;
    }

    @Override
    public double getCpuUsage() {
        //todo
        return 0;
    }

    @Override
    public AsyncTask<Double> getCpuUsageAsync() {
        //todo
        return null;
    }

    @Override
    public int getMemory() {
        //todo
        return 0;
    }

    @Override
    public AsyncTask<Integer> getMemoryAsync() {
        //todo
        return null;
    }

    @Override
    public long getBootTime() {
        //todo
        return 0;
    }
}
