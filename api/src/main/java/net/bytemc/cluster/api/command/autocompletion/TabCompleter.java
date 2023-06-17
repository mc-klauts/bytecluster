package net.bytemc.cluster.api.command.autocompletion;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import net.bytemc.cluster.api.command.interfaces.CommandSender;

public interface TabCompleter {

    List<String> EMPTY = new ArrayList<>();

    List<String> complete(
        CommandSender commandSender,
        String name,
        String[] args
    );

}
