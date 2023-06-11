package net.bytemc.bytecluster.wrapper.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.service.AbstractCloudService;

@Getter
@AllArgsConstructor
public final class WrapperCloudService extends AbstractCloudService {

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
