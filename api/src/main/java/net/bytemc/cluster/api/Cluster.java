package net.bytemc.cluster.api;

import lombok.Getter;
import net.bytemc.cluster.api.network.PacketPool;
import net.bytemc.cluster.api.network.QueryPacket;
import net.bytemc.cluster.api.network.packets.ServiceIdentifiyPacket;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceRequest;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceResponse;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;

@Getter
public abstract class Cluster {

    @Getter
    private static Cluster instance;

    private PacketPool packetPool;

    public Cluster() {
        instance = this;
        packetPool = new PacketPool();

        PacketPool.registerPackets(

                // general packets
                QueryPacket.class,

                // cloud logic packets
                ServiceIdentifiyPacket.class,

                // api packets
                SingletonServiceResponse.class,
                SingletonServiceRequest.class
        );
    }

    public abstract CloudServiceGroupProvider getServiceGroupProvider();

    public abstract CloudServiceProvider getServiceProvider();

}
