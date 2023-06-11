package net.bytemc.bytecluster.wrapper.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.service.CloudService;

@Getter
@AllArgsConstructor
public final class WrapperCloudService implements CloudService {

    private String name;
    private String hostname;
    private int port;
    private int id;
    private String groupName;
    private int maxPlayers;
    private String motd;

    @Override
    public int getPlayers() {
        //todo
        return 0;
    }

    @Override
    public void executeCommand(String command) {
        //todo
    }

    @Override
    public void shutdown() {
        //todo
    }
}
