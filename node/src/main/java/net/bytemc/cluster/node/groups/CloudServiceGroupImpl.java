package net.bytemc.cluster.node.groups;

import lombok.Getter;
import lombok.ToString;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.AbstractCloudServiceGroup;
import net.bytemc.cluster.api.service.CloudGroupType;
@Getter
@ToString
public final class CloudServiceGroupImpl extends AbstractCloudServiceGroup {

    public CloudServiceGroupImpl(String name, CloudGroupType groupType, int minOnlineCount, int maxOnlineCount, int maxMemory, boolean fallback, int defaultStartPort, String bootstrapNodes, boolean staticService) {
        super(name, groupType, minOnlineCount, maxOnlineCount, maxMemory, fallback, defaultStartPort, bootstrapNodes, staticService);
    }

    @Override
    public void shutdownAllServices() {
        for (var service : Cluster.getInstance().getServiceProvider().findServices(this.getName())) {
            service.shutdown();
        }
    }
}
