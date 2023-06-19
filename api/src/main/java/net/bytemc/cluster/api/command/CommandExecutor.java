package net.bytemc.cluster.api.command;

import java.util.Collections;
import java.util.List;
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

    public List<String> tryTabComplete(
        CommandSender commandSender,
        String callName,
        String input
    ) {
        for (InternalCommand command : Cluster.getInstance().getCommandRepository().getCommandMap()
            .values()) {
            if (command.getCallNames().contains(callName)) {
                return command.tryTabComplete(commandSender, input);
            }
        }
        return Collections.emptyList();
    }

}
