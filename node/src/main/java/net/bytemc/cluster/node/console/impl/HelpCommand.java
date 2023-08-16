package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.DefaultExecution;
import net.bytemc.cluster.api.command.commandsender.CommandSender;
import net.bytemc.cluster.api.logging.Logger;

@Command(name = "help", aliases = "?")
public final class HelpCommand {

    @DefaultExecution
    public void handle(CommandSender commandSender) {
        Logger.info("group &8| &7Manage all groups");
        Logger.info("service &8| &7Manage all services");
        Logger.info("reload &8| &7Reload the configuration and restart sub systems");
        Logger.info("shutdown &8| &7Shutdown the node");
        Logger.info("help &8| &7Show this help message and tips");
        Logger.info("clear &8| &7Clear the console screen");
    }

}
