package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.DefaultExecution;
import net.bytemc.cluster.api.command.commandsender.CommandSender;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.NodeShutdownHandler;
import org.jetbrains.annotations.NotNull;

@Command(name = "shutdown", aliases = {"exit", "stop"})
public final class ShutdownCommand {

    @DefaultExecution
    private void execute(@NotNull CommandSender commandSender) {
        NodeShutdownHandler.shutdown(Node.getInstance());
    }

}
