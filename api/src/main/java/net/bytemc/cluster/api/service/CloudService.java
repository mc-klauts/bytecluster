package net.bytemc.cluster.api.service;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.properties.Property;
import net.bytemc.cluster.api.properties.PropertyHolder;
import net.bytemc.cluster.api.statistics.StatisticsHolder;

import java.util.List;
import java.util.Map;

public interface CloudService extends PropertyHolder, StatisticsHolder {

    String getName();

    String getHostname();

    int getPort();

    int getId();

    String getGroupName();

    int getPlayers();

    AsyncTask<Integer> getPlayersAsync();

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
