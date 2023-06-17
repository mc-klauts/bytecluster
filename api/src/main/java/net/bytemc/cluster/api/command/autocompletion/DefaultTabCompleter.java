package net.bytemc.cluster.api.command.autocompletion;

import java.util.ArrayList;
import java.util.List;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DefaultTabCompleter implements TabCompleter {

    @Override
    public @NotNull List<String> complete(
        CommandSender commandSender,
        String name,
        String[] args
    ) {
        return new ArrayList<>();
    }
}
