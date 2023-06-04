package net.bytemc.cluster.api;

import lombok.Getter;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;

public abstract class Cluster {

    @Getter
    private static Cluster instance;

    public Cluster() {
        instance = this;
    }

    public abstract CloudServiceGroupProvider getServiceGroupProvider();

    public abstract CloudServiceProvider getServiceProvider();

}
