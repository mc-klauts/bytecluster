package net.bytemc.cluster.plugin.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;

public final class ByteClusterExternalCloudCommand extends Command {

    public ByteClusterExternalCloudCommand() {
        super("close");

        setCondition((sender, commandString) -> sender instanceof ConsoleSender);
        setDefaultExecutor((sender, context) -> {
            MinecraftServer.stopCleanly();
            System.exit(-1);
        });

        MinecraftServer.getCommandManager().register(this);
    }
}
