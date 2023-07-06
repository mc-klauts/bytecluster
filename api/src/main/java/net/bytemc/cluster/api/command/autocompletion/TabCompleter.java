package net.bytemc.cluster.api.command.autocompletion;

import java.util.ArrayList;
import java.util.List;
import net.bytemc.cluster.api.command.commandsender.CommandSender;

public interface TabCompleter {

    List<String> EMPTY = new ArrayList<>();

    List<String> complete(
        CommandSender commandSender,
        List<String> arguments
    );

}
