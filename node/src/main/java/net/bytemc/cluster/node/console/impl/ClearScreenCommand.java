package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.DefaultExecution;
import net.bytemc.cluster.api.command.commandsender.CommandSender;
import net.bytemc.cluster.node.Node;
import org.jetbrains.annotations.NotNull;

@Command(name = "clear", aliases = {"cs", "clearscreen"})
public final class ClearScreenCommand {

    @DefaultExecution
    private void execute(@NotNull CommandSender commandSender) {
        Node.getInstance().getConsoleTerminal().clearScreen();
    }
}
