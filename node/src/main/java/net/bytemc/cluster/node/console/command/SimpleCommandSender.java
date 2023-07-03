package net.bytemc.cluster.node.console.command;

import net.bytemc.cluster.api.command.commandsender.CommandSender;
import net.bytemc.cluster.api.command.commandsender.CommandSenderType;
import net.bytemc.cluster.api.logging.Logger;

public final class SimpleCommandSender implements CommandSender {

    @Override
    public void sendMessage(String text) {
        Logger.info(text);
    }

    @Override
    public void sendNonePermission() {

    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public CommandSenderType getType() {
        return CommandSenderType.CONSOLE;
    }
}
