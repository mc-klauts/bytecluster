package net.bytemc.cluster.node.console.command;

import net.bytemc.cluster.api.command.interfaces.CommandSender;

public final class SimpleCommandSender implements CommandSender {

    @Override
    public void sendMessage(String text) {
        System.out.println(text);
    }
}
