package net.bytemc.bytecluster.wrapper.groups;

import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;

import java.util.Collection;

public final class CloudServiceGroupProviderImpl implements CloudServiceGroupProvider {
    @Override
    public TaskFuture<Collection<CloudServiceGroup>> findGroupsAsync() {
        return null;
    }

    @Override
    public Collection<CloudServiceGroup> findGroups() {
        return null;
    }

    @Override
    public TaskFuture<CloudServiceGroup> findGroupAsync(String id) {
        return null;
    }

    @Override
    public CloudServiceGroup findGroup(String id) {
        return null;
    }

    @Override
    public void addGroup(CloudServiceGroup group) {

    }

    @Override
    public void removeGroup(String name) {

    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public TaskFuture<Boolean> existsAsync(String id) {
        return null;
    }
}