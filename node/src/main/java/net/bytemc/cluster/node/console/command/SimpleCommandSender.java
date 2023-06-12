package net.bytemc.cluster.node.console.command;

import net.bytemc.cluster.api.command.interfaces.CommandSender;
import net.bytemc.cluster.api.logging.Logger;

public final class SimpleCommandSender implements CommandSender {

    @Override
    public void sendMessage(String text) {
        Logger.info(text);
    }
}
