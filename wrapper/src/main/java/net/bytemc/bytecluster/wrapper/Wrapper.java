package net.bytemc.bytecluster.wrapper;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class Wrapper extends Cluster {


    @Contract(pure = true)
    @Override
    public @Nullable CloudServiceGroupProvider getServiceGroupProvider() {
        return null;
    }

    @Contract(pure = true)
    @Override
    public @Nullable CloudServiceProvider getServiceProvider() {
        return null;
    }
}
