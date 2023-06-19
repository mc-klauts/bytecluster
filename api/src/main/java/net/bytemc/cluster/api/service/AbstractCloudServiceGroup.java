package net.bytemc.cluster.api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractCloudServiceGroup implements CloudServiceGroup {

    private String name;
    private CloudGroupType groupType;
    private int minOnlineCount;
    private int maxOnlineCount;
    private int maxMemory;
    private boolean fallback;
    private String bootstrapNodes;

}
