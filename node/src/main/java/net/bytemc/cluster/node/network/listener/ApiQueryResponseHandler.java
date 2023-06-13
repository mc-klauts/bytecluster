package net.bytemc.cluster.node.network.listener;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.PacketPool;
import net.bytemc.cluster.api.network.packets.services.CollectionServiceRequest;
import net.bytemc.cluster.api.network.packets.services.CollectionServiceResponse;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceRequest;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceResponse;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.node.Node;

import java.util.List;

public final class ApiQueryResponseHandler {

    public ApiQueryResponseHandler() {

        var pool = Node.getInstance().getPacketPool();
        pool.addQueryModification(SingletonServiceRequest.class, (packet) -> {
            return new SingletonServiceResponse(Cluster.getInstance().getServiceProvider().findService(packet.getId()));
        });

        pool.addQueryModification(CollectionServiceRequest.class, (packet) -> {
            System.out.println("request is da call");
            switch (packet.getServiceFilter()) {
                case ALL -> {
                    return new CollectionServiceResponse(Cluster.getInstance().getServiceProvider().findServices());
                }
            }
            return null;
        });


    }
}
