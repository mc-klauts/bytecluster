package net.bytemc.bytecluster.wrapper.groups;

import net.bytemc.cluster.api.service.AbstractCloudServiceGroup;
import net.bytemc.cluster.api.service.CloudGroupType;

public final class WrapperCloudServiceGroup extends AbstractCloudServiceGroup {

    public WrapperCloudServiceGroup(String name, CloudGroupType groupType, int minOnlineCount, int maxOnlineCount, int maxMemory, boolean fallback, int defaultStartPort, String bootstrapNodes) {
        super(name, groupType, minOnlineCount, maxOnlineCount, maxMemory, fallback, defaultStartPort, bootstrapNodes);
    }

    @Override
    public void shutdownAllServices() {
        //todo
    }
}
