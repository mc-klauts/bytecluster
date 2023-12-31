package net.bytemc.bytecluster.wrapper;

import lombok.Getter;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.misc.statistics.CpuEvaluator;
import net.bytemc.cluster.api.misc.statistics.MemoryEvaluator;
import net.bytemc.cluster.api.network.packets.services.CloudServiceRequestPlayerAmountPacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceResponsePlayerAmountPacket;
import net.bytemc.cluster.api.network.packets.services.WrapperRequestServiceCommandPacket;
import net.bytemc.cluster.api.network.packets.services.WrapperRequestServiceShutdownPacket;
import net.bytemc.cluster.api.properties.LocalProperty;
import net.bytemc.cluster.api.properties.Property;
import net.bytemc.cluster.api.service.AbstractCloudService;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class WrapperLocalCloudService extends AbstractCloudService {

    @Getter
    private long bootTime;
    private Map<String, Property<?>> properties = new ConcurrentHashMap<>();


    public WrapperLocalCloudService(String hostname, String groupName, String motd, int port, int id, int maxPlayers, CloudServiceState state) {
        super(hostname, groupName, motd, port, id, maxPlayers, state);

        this.bootTime = Wrapper.getInstance().getBootTime();
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
        this.properties.remove(id);
    }

    @Override
    public <T> T setProperty(String id, T value) {
        this.properties.put(id, new LocalProperty<>(value));
        return value;
    }

    @Override
    public <T> @NotNull AsyncTask<Property<T>> requestPropertyAsync(String id) {
        return AsyncTask.directly(requestProperty(id));
    }

    @Override
    public <T> Property<T> requestProperty(String id) {
        return (Property<T>) properties.get(id);
    }

    @Override
    public double getCpuUsage() {
        return CpuEvaluator.processCpuLoad();
    }

    @Override
    public AsyncTask<Double> getCpuUsageAsync() {
        return AsyncTask.directly(getCpuUsage());
    }

    @Override
    public int getMemory() {
        return MemoryEvaluator.getCurrent();
    }

    @Override
    public AsyncTask<Integer> getMemoryAsync() {
        return AsyncTask.directly(getMemory());
    }

    @Contract(pure = true)
    public static @Nullable CloudService toSelfService(CloudService service) {
        return new WrapperLocalCloudService(service.getHostname(), service.getGroupName(), service.getMotd(), service.getPort(), service.getId(), service.getMaxPlayers(), service.getState());
    }
}
