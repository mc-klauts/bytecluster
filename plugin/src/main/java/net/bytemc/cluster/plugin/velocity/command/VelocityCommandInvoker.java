package net.bytemc.cluster.plugin.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import net.bytemc.cluster.api.command.commandsender.CommandSender;
import net.bytemc.cluster.api.command.commandsender.CommandSenderType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public record VelocityCommandInvoker(CommandSource source) implements CommandSender {

    @Override
    public void sendMessage(String text) {
        source.sendMessage(Component.text(text));
    }

    @Override
    public void sendNonePermission() {
        source.sendMessage(Component.text("No permission!"));
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return permission.equals("") || source.hasPermission(permission);
    }

    @Override
    public CommandSenderType getType() {
        return CommandSenderType.PLAYER;
    }
}
