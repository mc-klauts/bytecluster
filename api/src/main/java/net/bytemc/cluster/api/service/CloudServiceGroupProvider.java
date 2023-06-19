package net.bytemc.cluster.api.service;

import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.Collection;

public interface CloudServiceGroupProvider {

    AsyncTask<Collection<CloudServiceGroup>> findGroupsAsync();

    Collection<CloudServiceGroup> findGroups();

    AsyncTask<CloudServiceGroup> findGroupAsync(String id);

    CloudServiceGroup findGroup(String id);

    void addGroup(CloudServiceGroup group);

    void removeGroup(String name);

    boolean exists(String id);

    AsyncTask<Boolean> existsAsync(String id);

    CloudServiceGroup getCloudServiceGroupFromBuffer(PacketBuffer buffer);

}
