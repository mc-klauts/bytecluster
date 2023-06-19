package net.bytemc.bytecluster.wrapper.groups;

import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import java.util.Collection;

public final class CloudServiceGroupProviderImpl implements CloudServiceGroupProvider {
    @Override
    public AsyncTask<Collection<CloudServiceGroup>> findGroupsAsync() {
        //todo
        return null;
    }

    @Override
    public Collection<CloudServiceGroup> findGroups() {
        //todo
        return null;
    }

    @Override
    public AsyncTask<CloudServiceGroup> findGroupAsync(String id) {
        //todo
        return null;
    }

    @Override
    public CloudServiceGroup findGroup(String id) {
        //todo
        return null;
    }

    @Override
    public void addGroup(CloudServiceGroup group) {
        //todo
    }

    @Override
    public void removeGroup(String name) {
        //todo
    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public AsyncTask<Boolean> existsAsync(String id) {
        //todo
        return null;
    }
}
