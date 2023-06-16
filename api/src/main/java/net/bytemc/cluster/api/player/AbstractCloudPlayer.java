package net.bytemc.cluster.api.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class AbstractCloudPlayer implements CloudPlayer {

    private final String name;
    private final UUID uniqueId;

}
