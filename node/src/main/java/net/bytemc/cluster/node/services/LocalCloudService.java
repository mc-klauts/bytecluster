package net.bytemc.cluster.node.services;

import io.netty5.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.service.AbstractCloudService;
import net.bytemc.cluster.api.service.CloudServiceState;
import net.bytemc.cluster.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

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
        Cluster.getInstance().getServiceProvider().getFactory().stop(this);
    }

    public @NotNull Path getDirectory() {
        return Node.getInstance().getRuntimeConfiguration().getNodePath().getServerRunningPath().resolve(getName());
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
}
