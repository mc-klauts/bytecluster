package net.bytemc.cluster.api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractCloudService implements CloudService {

    private String hostname;

    // not save the complete object, only the name, because only the service process is faster
    private String groupName;

    // it's only a virtual motd, not the motd with the players
    private String motd;

    private int port;
    private int id;
    private int maxPlayers;

    @Getter
    private CloudServiceState state;

    public String getName() {
        return groupName + "-" + id;
    }
}


