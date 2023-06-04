package net.bytemc.cluster.node.groups;

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
    public TaskFuture<CloudServiceGroup> findGroupAsync(String name) {
        return null;
    }

    @Override
    public CloudServiceGroup findGroup(String name) {
        return null;
    }

    @Override
    public void addGroup(CloudServiceGroup group) {

    }

    @Override
    public void removeGroup(String name) {

    }
}
