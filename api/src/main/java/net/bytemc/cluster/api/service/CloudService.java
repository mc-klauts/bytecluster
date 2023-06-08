package net.bytemc.cluster.api.service;

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


    void shutdown();

    CloudServiceGroup getGroup();

    TaskFuture<CloudServiceGroup> getGroupAsync();

}
