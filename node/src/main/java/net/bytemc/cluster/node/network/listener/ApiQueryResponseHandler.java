package net.bytemc.cluster.node.network.listener;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.packets.player.*;
import net.bytemc.cluster.api.network.packets.services.*;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.node.Node;

import java.util.UUID;

public final class ApiQueryResponseHandler {

    public ApiQueryResponseHandler() {

        var pool = Node.getInstance().getPacketPool();
        pool.addQueryModification(SingletonServiceRequest.class, (packet) -> {
            return new SingletonServiceResponse(Cluster.getInstance().getServiceProvider().findService(packet.getId()));
        });

        pool.addQueryModification(CollectionServiceRequest.class, (packet) -> {
            switch (packet.getServiceFilter()) {
                case ALL -> {
                    return new CollectionServiceResponse(Cluster.getInstance().getServiceProvider().findServices());
                }
                case NON_PROXIES -> {
                    return new CollectionServiceResponse(Cluster.getInstance().getServiceProvider().findServices().stream().filter(it -> !it.getGroup().getGroupType().isProxy()).toList());
                }
            }
            return null;
        });

        pool.addQueryModification(FindFallbackServiceRequest.class, (packet) -> {
            return new FindFallbackServiceResponse(Cluster.getInstance().getServiceProvider().findFallback().stream().map(CloudService::getName).findFirst());
        });

        pool.addQueryModification(SingletonPlayerRequest.class, (packet) -> {
            if (packet.getIdentifier() instanceof UUID uuid) {
                return new SingletonPlayerResponse(Cluster.getInstance().getPlayerHandler().findPlayer(uuid).orElse(null));
            } else {
                return new SingletonPlayerResponse(Cluster.getInstance().getPlayerHandler().findPlayer((String) packet.getIdentifier()).orElse(null));
            }
        });

        pool.addQueryModification(CloudPlayerAmountRequest.class, (packet) -> {
            return new CloudPlayerAmountResponse(Cluster.getInstance().getPlayerHandler().getOnlineCount());
        });

        pool.addQueryModification(CollectionCloudPlayerRequest.class, (packet) -> {
            return new CollectionCloudPlayerResponse(Cluster.getInstance().getPlayerHandler().findPlayers());
        });
    }
}
