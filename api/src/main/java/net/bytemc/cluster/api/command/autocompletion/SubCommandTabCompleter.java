package net.bytemc.cluster.api.command.autocompletion;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.command.IndexedCommand;
import net.bytemc.cluster.api.command.commandsender.CommandSender;

@RequiredArgsConstructor
public final class SubCommandTabCompleter implements TabCompleter {

    private final IndexedCommand indexedCommand;

    @Override
    public List<String> complete(
        CommandSender commandSender,
        List<String> arguments
    ) {
        return indexedCommand.getSubCommandMap().keySet().stream().toList();
    }
}
