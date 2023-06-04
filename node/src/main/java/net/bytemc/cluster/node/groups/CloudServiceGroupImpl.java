package net.bytemc.cluster.node.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.service.CloudServiceGroup;

@Getter
@AllArgsConstructor
public final class CloudServiceGroupImpl implements CloudServiceGroup {

    private String name;
    private int minOnlineCount;
    private int maxOnlineCount;
    private int maxMemory;
    private boolean fallback;

    @Override
    public void shutdownAllServices() {
        //todo
    }
}
