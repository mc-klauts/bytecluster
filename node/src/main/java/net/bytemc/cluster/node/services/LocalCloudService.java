package net.bytemc.cluster.node.services;

import lombok.Getter;
import lombok.Setter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Getter
public final class LocalCloudService implements CloudService {

    private String groupName;
    private String hostname;

    private int id;
    private int port;

    private int maxPlayers;
    private String motd;

    @Nullable @Setter
    private Process process;

    private CloudServiceState state = CloudServiceState.OPEN;

    public LocalCloudService(String groupName, String hostname, int id, int port, int maxPlayers, String motd) {
        this.groupName = groupName;
        this.hostname = hostname;
        this.id = id;
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.motd = motd;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getName() {
        return groupName + "-" + id;
    }


    @Override
    public int getPlayers() {
        //todo
        return 0;
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

    @Override
    public CloudServiceGroup getGroup() {
        return Cluster.getInstance().getServiceGroupProvider().findGroup(this.groupName);
    }

    @Override
    public @NotNull TaskFuture<CloudServiceGroup> getGroupAsync() {
        return TaskFuture.instantly(getGroup());
    }

    public @NotNull Path getDirectory() {
        return Path.of("temp", getName());
    }
}
