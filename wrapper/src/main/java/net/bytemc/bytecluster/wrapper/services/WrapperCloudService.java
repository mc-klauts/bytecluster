package net.bytemc.bytecluster.wrapper.services;

import lombok.Getter;
import net.bytemc.cluster.api.service.AbstractCloudService;
import net.bytemc.cluster.api.service.CloudServiceState;

@Getter
public final class WrapperCloudService extends AbstractCloudService {

    public WrapperCloudService(String name, String hostname, String groupName, String motd, int port, int id, int maxPlayers, CloudServiceState state) {
        super(name, hostname, groupName, motd, port, id, maxPlayers, state);
    }

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
