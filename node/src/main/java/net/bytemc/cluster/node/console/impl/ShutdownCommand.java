package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.DefaultCommand;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.NodeShutdownHandler;
import org.jetbrains.annotations.NotNull;

@Command(name = "shutdown", aliases = {"exit"})
public final class ShutdownCommand {

    @DefaultCommand
    private void execute(@NotNull CommandSender commandSender) {
        NodeShutdownHandler.shutdown(Node.getInstance());
    }

}
