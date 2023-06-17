package net.bytemc.cluster.node.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceGroup;

@Getter
@AllArgsConstructor
@ToString
public final class CloudServiceGroupImpl implements CloudServiceGroup {

    private String name;

    private CloudGroupType groupType;

    private int minOnlineCount;
    private int maxOnlineCount;
    private int maxMemory;
    private boolean fallback;

    @Override
    public void shutdownAllServices() {
        for (var service : Cluster.getInstance().getServiceProvider().findServices(this.name)) {
            service.shutdown();
        }
    }
}
