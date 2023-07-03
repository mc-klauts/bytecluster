package net.bytemc.cluster.node.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.command.commandsender.CommandSender;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.node.console.command.SimpleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

public class ConsoleLineCompleter implements Completer {

    private final CommandSender commandSender = new SimpleCommandSender();

    @Override
    public void complete(
        LineReader lineReader,
        @NotNull ParsedLine parsedLine,
        List<Candidate> list
    ) {
        final List<String> words = new ArrayList<>(parsedLine.words());

        if (words.isEmpty() || words.size() == 1) {
            list.addAll(
                Cluster.getInstance().getCommandRepository().findAll().stream()
                    .map(command -> new Candidate(command.getName())).toList());
            return;
        }

        final String alias = words.remove(0);
        Cluster.getInstance().getCommandRepository().findOptional(alias)
            .ifPresent(
                indexedCommand -> indexedCommand.complete(commandSender, words)
                    .forEach(s -> list.add(new Candidate(s))));
    }
}
