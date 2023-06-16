package net.bytemc.cluster.api.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractCloudPlayer implements CloudPlayer {

    private final String name;
    private final String uniqueId;

}
