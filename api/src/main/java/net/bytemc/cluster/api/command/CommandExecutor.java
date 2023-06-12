package net.bytemc.cluster.api.command;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.command.interfaces.CommandSender;

public final class CommandExecutor {

    public void tryExecution(
        CommandSender commandSender,
        String input
    ) {
        for (InternalCommand command : Cluster.getInstance().getCommandRepository().getCommandMap()
            .values()) {
            if (command.tryExecute(commandSender, input)) {
                return;
            }
        }
    }

}
