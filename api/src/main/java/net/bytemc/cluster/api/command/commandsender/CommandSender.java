package net.bytemc.cluster.api.command.commandsender;

public interface CommandSender {

    void sendMessage(String text);

    void sendNonePermission();

    boolean hasPermission(String permission);

    CommandSenderType getType();

}
