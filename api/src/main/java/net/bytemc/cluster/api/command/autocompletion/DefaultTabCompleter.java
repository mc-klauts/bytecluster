package net.bytemc.cluster.api.command.autocompletion;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import net.bytemc.cluster.api.command.commandsender.CommandSender;

public class DefaultTabCompleter implements TabCompleter {

    @Override
    public @NotNull List<String> complete(
        CommandSender commandSender,
        List<String> arguments
    ) {
        return EMPTY;
    }
}
