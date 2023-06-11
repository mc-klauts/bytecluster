package net.bytemc.cluster.api.service;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.misc.TaskFuture;

public interface CloudService {

    String getName();

    String getHostname();

    int getPort();

    int getId();

    String getGroupName();

    int getPlayers();

    int getMaxPlayers();

    String getMotd();

    CloudServiceState getState();

    void executeCommand(String command);

    void shutdown();

    default CloudServiceGroup getGroup() {
        return Cluster.getInstance().getServiceGroupProvider().findGroup(this.getGroupName());
    }

    default TaskFuture<CloudServiceGroup> getGroupAsync() {
        return TaskFuture.instantly(getGroup());
    }
}
