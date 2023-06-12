package net.bytemc.cluster.node.console.command;

import net.bytemc.cluster.api.command.interfaces.CommandSender;
import net.bytemc.cluster.node.logger.Logger;

public final class SimpleCommandSender implements CommandSender {

    @Override
    public void sendMessage(String text) {
        Logger.info(text);
    }
}
