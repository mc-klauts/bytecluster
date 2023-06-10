package net.bytemc.bytecluster.wrapper;

import lombok.Getter;
import net.bytemc.bytecluster.wrapper.groups.CloudServiceProviderImpl;
import net.bytemc.bytecluster.wrapper.network.NettyClient;
import net.bytemc.bytecluster.wrapper.services.CloudServiceGroupProviderImpl;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.PacketPool;
import net.bytemc.cluster.api.network.packets.ServiceIdentifiyPacket;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@Getter
public final class Wrapper extends Cluster {

    private final CloudServiceGroupProvider serviceGroupProvider;
    private final CloudServiceProvider serviceProvider;

    private PacketPool packetPool = new PacketPool();
    private final NettyClient client;

    public Wrapper(String id) {

        this.packetPool.registerPacket(ServiceIdentifiyPacket.class);

        this.serviceGroupProvider = new CloudServiceGroupProviderImpl();
        this.serviceProvider = new CloudServiceProviderImpl();

        this.client = new NettyClient("proxy-1");
    }

    public void connect() {
        this.client.connect().onComplete(s -> {
            System.out.println("Polo");
            WrapperLauncher.getWrapperThread().start();
        }).onCancel(s -> {
            System.exit(-1);
        });
    }

}
