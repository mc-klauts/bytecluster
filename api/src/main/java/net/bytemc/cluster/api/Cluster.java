package net.bytemc.cluster.api;

import lombok.Getter;
import net.bytemc.cluster.api.command.CommandRepository;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;

@Getter
public abstract class Cluster {

    @Getter
    private static Cluster instance;

    @Getter
    private final CommandRepository commandRepository = new CommandRepository();

    private final PacketPool packetPool;

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
