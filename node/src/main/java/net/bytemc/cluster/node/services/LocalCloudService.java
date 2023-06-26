package net.bytemc.cluster.node.services;

import io.netty5.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.services.CloudServiceShutdownEvent;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.misc.GsonHelper;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.QueryPacket;
import net.bytemc.cluster.api.network.packets.properties.PropertyDeletePacket;
import net.bytemc.cluster.api.network.packets.properties.PropertyRequestSharePacket;
import net.bytemc.cluster.api.network.packets.properties.PropertySetPacket;
import net.bytemc.cluster.api.network.packets.properties.PropertySharePacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceCpuPacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceCpuRequestPacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceMemoryPacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceMemoryRequestPacket;
import net.bytemc.cluster.api.properties.Property;
import net.bytemc.cluster.api.service.AbstractCloudService;
import net.bytemc.cluster.api.service.CloudServiceState;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.event.CloudEventHandlerImpl;
import net.bytemc.cluster.node.templates.ServiceTemplateHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public final class LocalCloudService extends AbstractCloudService {

    @Nullable
    @Setter
    private Process process;

    // duplicate this entry, because not allow to modify the original api source code
    @Setter
    private CloudServiceState state = CloudServiceState.OPEN;

    @Nullable
    @Setter
    private Channel channel;

    public LocalCloudService(String hostname, String groupName, String motd, int port, int id, int maxPlayers) {
        super(hostname, groupName, motd, port, id, maxPlayers, null);
    }

    @Override
    public int getPlayers() {
        return Cluster.getInstance().getPlayerHandler().findPlayers().stream().map(it -> it.getCurrentServerId().equals(getName())).toList().size();
    }

    @Override
    public AsyncTask<Integer> getPlayersAsync() {
        return AsyncTask.directly(getPlayers());
    }

    @Override
    public void executeCommand(String command) {
        if (this.process != null) {
            final var outputStream = this.process.getOutputStream();
            try {
                outputStream.write((command + "\n").getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() {
        if (state != CloudServiceState.STOPPED) {
            setState(CloudServiceState.STOPPED);
            Cluster.getInstance().getServiceProvider().getFactory().stop(this);
            CloudServiceProviderImpl serviceProvider = (CloudServiceProviderImpl) Cluster.getInstance().getServiceProvider();

            if (channel != null) {
                serviceProvider.getServiceChannels().remove(this.channel);
            }

            // unregister events
            ((CloudEventHandlerImpl) Node.getInstance().getEventHandler()).removeCloudService(this);

            Cluster.getInstance().getEventHandler().call(new CloudServiceShutdownEvent(this));

            serviceProvider.removeService(getName());

            if (Node.getInstance().isRunning()) {
                ((CloudServiceProviderImpl) Cluster.getInstance().getServiceProvider()).getQueue().checkQueue();
            }
        }
    }

    public @NotNull Path getDirectory() {
        return (getGroup().isStaticService() ? ServiceTemplateHandler.STATIC_SERVICE_PATH : CloudServiceFactoryQueue.TEMP_PATH).resolve(getName());
    }

    @Override
    public CloudServiceState getState() {
        return this.state;
    }

    public void sendPacket(Packet packet) {
        if (this.channel != null && this.channel.isActive()) {
            this.channel.writeAndFlush(packet);
        } else {
            Logger.warn("Try to send " + packet.getClass().getSimpleName() + " to " + getName() + ", but the channel is not active.");
        }
    }

    public <T extends Packet, R extends Packet> void sendQueryPacket(Packet packet, Class<R> responseType, Consumer<R> response) {
        var id = UUID.randomUUID();
        Node.getInstance().getPacketPool().saveResponse(id, response);
        this.sendPacket(new QueryPacket(id, packet));
    }


    @Override
    public void removeProperty(String id) {
        sendPacket(new PropertyDeletePacket(id));
    }

    @Override
    public <T> T setProperty(String id, T value) {
        sendPacket(new PropertySetPacket(id, GsonHelper.SENDABLE_GSON.toJson(value)));
        return value;
    }

    @Override
    public <T> AsyncTask<Property<T>> requestPropertyAsync(String id) {
        var task = new AsyncTask<Property<T>>();
       sendQueryPacket(new PropertyRequestSharePacket(id), PropertySharePacket.class, packet -> task.complete(new CloudServiceProperty<>(id, packet.getType(), packet.getPropertyAsString())));
        return task;
    }

    @Override
    public <T> Property<T> requestProperty(String id) {
        return (Property<T>) this.requestPropertyAsync(id).getSync(null);
    }

    @Override
    public double getCpuUsage() {
        return getCpuUsageAsync().getSync(null);
    }

    @Override
    public AsyncTask<Double> getCpuUsageAsync() {
        var task = new AsyncTask<Double>();
        sendQueryPacket(new CloudServiceCpuRequestPacket(getName()), CloudServiceCpuPacket.class, packet -> task.complete(packet.getCpu()));
        return task;
    }

    @Override
    public int getMemory() {
        return getMemoryAsync().getSync(null);
    }

    @Override
    public AsyncTask<Integer> getMemoryAsync() {
        var task = new AsyncTask<Integer>();
        sendQueryPacket(new CloudServiceMemoryRequestPacket(getName()), CloudServiceMemoryPacket.class, packet -> task.complete(packet.getMemory()));
        return task;
    }

    @Override
    public long getBootTime() {
        //todo
        return 0;
    }
}
