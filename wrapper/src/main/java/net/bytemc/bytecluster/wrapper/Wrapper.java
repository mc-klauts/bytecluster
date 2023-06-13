package net.bytemc.bytecluster.wrapper;

import lombok.Getter;
import net.bytemc.bytecluster.wrapper.event.WrapperEventHandler;
import net.bytemc.bytecluster.wrapper.logging.WrapperLogging;
import net.bytemc.bytecluster.wrapper.services.CloudServiceProviderImpl;
import net.bytemc.bytecluster.wrapper.network.NettyClient;
import net.bytemc.bytecluster.wrapper.groups.CloudServiceGroupProviderImpl;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.EventHandler;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.QueryPacket;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;

import java.util.UUID;
import java.util.function.Consumer;

@Getter
public final class Wrapper extends Cluster {

    @Getter
    private static Wrapper instance;

    private final Logger logger;
    private final EventHandler eventHandler;
    private final CloudServiceGroupProvider serviceGroupProvider;
    private final CloudServiceProvider serviceProvider;
    private final NettyClient client;

    public Wrapper(String id) {
        instance = this;

        this.logger = new WrapperLogging();

        this.eventHandler = new WrapperEventHandler();
        this.serviceGroupProvider = new CloudServiceGroupProviderImpl();
        this.serviceProvider = new CloudServiceProviderImpl();

        this.client = new NettyClient(id);
    }

    public <T extends Packet> void sendQueryPacket(T packet, Consumer<T> response) {
        this.sendQueryPacket(packet, (Class<T>) packet.getClass(), response);
    }

    public <T extends Packet, R extends Packet> void sendQueryPacket(Packet packet, Class<R> responseType, Consumer<R> response) {
        var id = UUID.randomUUID();
        this.getPacketPool().saveResponse(id, response);
        this.sendPacket(new QueryPacket(id, packet));
    }

    public void sendPacket(Packet packet) {
        this.client.sendPacket(packet);
    }

    public void connect() {
        this.client.connect().onComplete(s -> {

            // it is important to use sync methods only in platform threads

            WrapperLauncher.getWrapperThread().start();
        }).onCancel(s -> System.exit(-1));
    }
}
