package net.bytemc.cluster.node.console.commands;

import lombok.Getter;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.console.impl.ClearCommand;
import net.bytemc.cluster.node.console.impl.GroupCommand;
import net.bytemc.cluster.node.console.impl.ShutdownCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class SimpleCommandHandler implements CommandHandler {

    @Getter
    public final Map<String, CloudCommand> cachedCloudCommands;

    public SimpleCommandHandler() {
        this.cachedCloudCommands = new HashMap<>();

        this.registerCommand(new ShutdownCommand());
        this.registerCommand(new GroupCommand());
        this.registerCommand(new ClearCommand());
    }

    public void execute(final @NotNull String command) {
        final var args = new ArrayList<>(Arrays.asList(command.split(" ")));

        final var cloudCommand = this.cachedCloudCommands.get(args.get(0));
        if (cloudCommand == null) return;
        args.remove(0);
        cloudCommand.execute(Node.getInstance(), args.toArray(new String[]{}));
    }

    @Override
    public void registerCommand(final @NotNull CloudCommand command) {
        this.cachedCloudCommands.put(command.getName(), command);
        for (final var alias : command.getAliases()) this.cachedCloudCommands.put(alias, command);
    }

    @Override
    public void registerCommands(@NotNull CloudCommand... commands) {
        for (final var command : commands) this.registerCommand(command);
    }

    @Override
    public void unregisterCommand(final @NotNull CloudCommand command) {
        this.cachedCloudCommands.forEach((s, cloudCommand) -> {
            if (cloudCommand == command) this.cachedCloudCommands.remove(s);
        });
    }
}