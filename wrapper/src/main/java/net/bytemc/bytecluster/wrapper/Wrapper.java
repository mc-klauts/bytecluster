package net.bytemc.bytecluster.wrapper;

import lombok.Getter;
import net.bytemc.bytecluster.wrapper.groups.CloudServiceProviderImpl;
import net.bytemc.bytecluster.wrapper.network.NettyClient;
import net.bytemc.bytecluster.wrapper.services.CloudServiceGroupProviderImpl;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@Getter
public final class Wrapper extends Cluster {

    private final CloudServiceGroupProvider serviceGroupProvider;
    private final CloudServiceProvider serviceProvider;

    private final NettyClient client;

    public Wrapper(String id) {


        this.serviceGroupProvider = new CloudServiceGroupProviderImpl();
        this.serviceProvider = new CloudServiceProviderImpl();

        this.client = new NettyClient("prx");

        this.client.connect().onCancel(s -> WrapperLauncher.getWrapperThread().start()).onCancel(s -> {
            System.exit(-1);
        });
    }
}
