package net.bytemc.cluster.node.network.listener;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceRequest;
import net.bytemc.cluster.api.network.packets.services.SingletonServiceResponse;
import net.bytemc.cluster.node.Node;

public final class ApiQueryResponseHandler {

    public ApiQueryResponseHandler() {

        Node.getInstance().getPacketPool().addQueryModification(SingletonServiceRequest.class, (packet) -> {
            return new SingletonServiceResponse(Cluster.getInstance().getServiceProvider().findService(packet.getId()));
        });



    }
}
