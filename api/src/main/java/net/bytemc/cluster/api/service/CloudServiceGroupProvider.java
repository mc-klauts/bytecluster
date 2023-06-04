package net.bytemc.cluster.api.service;

import net.bytemc.cluster.api.misc.TaskFuture;

import java.util.Collection;

public interface CloudServiceGroupProvider {

    TaskFuture<Collection<CloudServiceGroup>> findGroupsAsync();

    Collection<CloudServiceGroup> findGroups();

    TaskFuture<CloudServiceGroup> findGroupAsync(String name);

    CloudServiceGroup findGroup(String name);

    void addGroup(CloudServiceGroup group);

    void removeGroup(String name);

}
