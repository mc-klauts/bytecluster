package net.bytemc.bytecluster.wrapper;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;

public final class Wrapper extends Cluster {


    @Override
    public CloudServiceGroupProvider getServiceGroupProvider() {
        return null;
    }

    @Override
    public CloudServiceProvider getServiceProvider() {
        return null;
    }
}
