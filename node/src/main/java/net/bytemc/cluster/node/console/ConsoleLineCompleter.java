package net.bytemc.cluster.node.console;

import java.util.List;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import net.bytemc.cluster.node.Node;
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
        if (parsedLine.words().isEmpty()) {
            return;
        }

        if (parsedLine.words().size() == 1) {
            list.addAll(
                Cluster.getInstance().getCommandRepository().getCommandMap().values().stream()
                    .map(command -> new Candidate(command.getCallNames().get(0))).toList());
            return;
        }

        final String callName = parsedLine.words().get(0);
        for (String s : Node.getInstance().getCommandExecutor()
            .tryTabComplete(this.commandSender, callName, parsedLine.line())) {
            list.add(new Candidate(s));
        }
    }
}
