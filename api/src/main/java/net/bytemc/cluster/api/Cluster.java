package net.bytemc.cluster.api;

import lombok.Getter;
import net.bytemc.cluster.api.event.EventHandler;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.command.CommandRepository;
import net.bytemc.cluster.api.network.PacketPool;
import net.bytemc.cluster.api.player.CloudPlayer;
import net.bytemc.cluster.api.player.CloudPlayerHandler;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;

@Getter
public abstract class Cluster {

    @Getter
    private static Cluster instance;

    private final PacketPool packetPool = new PacketPool();

    @Getter
    private final CommandRepository commandRepository = new CommandRepository();


    public Cluster() {
        instance = this;
    }

    public abstract Logger getLogger();

    public abstract EventHandler getEventHandler();

    public abstract CloudServiceGroupProvider getServiceGroupProvider();

    public abstract CloudServiceProvider getServiceProvider();

    public abstract CloudPlayerHandler getPlayerHandler();

}
