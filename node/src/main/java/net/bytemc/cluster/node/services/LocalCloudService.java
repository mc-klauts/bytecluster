package net.bytemc.cluster.node.services;

import lombok.Getter;
import lombok.Setter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceState;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public String getName() {
        return groupName + "-" + id;
    }


    @Override
    public int getPlayers() {
        //todo
        return 0;
    }

    @Override
    public void shutdown() {
        // todo
    }

    @Override
    public CloudServiceGroup getGroup() {
        return Cluster.getInstance().getServiceGroupProvider().findGroup(this.groupName);
    }

    @Override
    public TaskFuture<CloudServiceGroup> getGroupAsync() {
        return TaskFuture.instantly(getGroup());
    }

    public Path getDirectory() {
        return Path.of("temp", getName());
    }
}
