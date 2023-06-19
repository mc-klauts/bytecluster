package net.bytemc.cluster.node.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.AbstractCloudServiceGroup;
import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceGroup;

@Getter
@ToString
public final class CloudServiceGroupImpl extends AbstractCloudServiceGroup {


    public CloudServiceGroupImpl(String name, CloudGroupType groupType, int minOnlineCount, int maxOnlineCount, int maxMemory, boolean fallback, String bootstrapNodes) {
        super(name, groupType, minOnlineCount, maxOnlineCount, maxMemory, fallback, bootstrapNodes);
    }

    @Override
    public void shutdownAllServices() {
        for (var service : Cluster.getInstance().getServiceProvider().findServices(this.getName())) {
            service.shutdown();
        }
    }
}
