package net.bytemc.bytecluster.wrapper;

import lombok.Getter;
import net.bytemc.bytecluster.wrapper.services.CloudServiceProviderImpl;
import net.bytemc.bytecluster.wrapper.network.NettyClient;
import net.bytemc.bytecluster.wrapper.groups.CloudServiceGroupProviderImpl;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.PacketPool;
import net.bytemc.cluster.api.network.QueryPacket;
import net.bytemc.cluster.api.network.packets.ServiceIdentifiyPacket;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;

import java.util.UUID;
import java.util.function.Consumer;

@Getter
public final class Wrapper extends Cluster {

    @Getter
    private static Wrapper instance;

    private final CloudServiceGroupProvider serviceGroupProvider;
    private final CloudServiceProvider serviceProvider;
    private final NettyClient client;

    public Wrapper(String id) {
        instance = this;
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
            Cluster.getInstance().getServiceProvider().findServiceAsync("Lobby-1").onComplete(service -> {
                System.out.println(service.getMotd());
            });
            WrapperLauncher.getWrapperThread().start();
        }).onCancel(s -> System.exit(-1));
    }
}
