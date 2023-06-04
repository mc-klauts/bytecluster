package net.bytemc.cluster.node.console.commands;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface CommandHandler {

    void registerCommand(@NotNull CloudCommand command);

    void registerCommands(@NotNull CloudCommand... commands);

    void unregisterCommand(@NotNull CloudCommand command);

    void execute(@NotNull String command);

    @NotNull Map<String, CloudCommand> getCachedCloudCommands();

}
