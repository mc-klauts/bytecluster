package net.bytemc.cluster.plugin.velocity.command;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.command.CommandRepository;
import net.bytemc.cluster.api.command.IndexedCommand;

@RequiredArgsConstructor
public final class VelocityCommandSupport {

    @Getter
    private final CommandRepository commandRepository = Cluster.getInstance().getCommandRepository();
    private final ProxyServer proxyServer;

    public void registerAllCommandsOnVelocity() {
        Cluster.getInstance().getCommandRepository().setOnCommandRegister(this::register);
        this.commandRepository.findAll().forEach(this::register);
    }

    private void register(IndexedCommand indexedCommand) {
        this.proxyServer.getCommandManager().register(indexedCommand.getName(),
                new VelocityClusterCommand(this.commandRepository), indexedCommand.getAliases());
    }
}
