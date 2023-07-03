package net.bytemc.cluster.plugin.velocity.command;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.command.CommandRepository;

@RequiredArgsConstructor
public final class VelocityCommandSupport {

    @Getter
    private final CommandRepository commandRepository = new CommandRepository();
    private final ProxyServer proxyServer;

    public void registerAllCommandsOnVelocity() {
        this.commandRepository.findAll()
            .forEach(indexedCommand -> this.proxyServer.getCommandManager()
                .register(indexedCommand.getName(),
                    new VelocityClusterCommand(this.commandRepository),
                    indexedCommand.getAliases()));
    }
}
