package net.bytemc.cluster.api.service;

import java.util.List;

public interface CloudServiceGroupFactory {

    List<CloudServiceGroup> loadGroups();

    boolean create(CloudServiceGroup cloudServiceGroup);

    void remove(CloudServiceGroup group);

    boolean existInStorage(CloudServiceGroup group);
}
