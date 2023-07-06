package net.bytemc.cluster.node.console.impl;

import java.util.List;
import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.CommandParameter;
import net.bytemc.cluster.api.command.annotations.SubCommand;
import net.bytemc.cluster.api.command.argument.transformers.StringArgumentTransformer;
import net.bytemc.cluster.api.command.autocompletion.TabCompleter;
import net.bytemc.cluster.api.command.commandsender.CommandSender;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.node.Node;
import org.jetbrains.annotations.NotNull;

@Command(name = "service")
public final class ServiceCommand {

    @SubCommand(name = "shutdown", tabCompleter = ServiceNameTabCompleter.class)
    private void startServer(
        CommandSender commandSender,
        @CommandParameter(name = "name", transformer = StringArgumentTransformer.class) String name
    ) {
        final var serviceProvider = Node.getInstance().getServiceProvider();
        CloudService service = serviceProvider.findService(name);
        if (service == null) {
            commandSender.sendMessage("There is no service with this name&8.");
            return;
        }
        commandSender.sendMessage("Trying to shutdown " + service.getName() + " service&8...");
        service.shutdown();
    }

    public static class ServiceNameTabCompleter implements TabCompleter {


        @Override
        public List<String> complete(
            CommandSender commandSender,
            @NotNull List<String> arguments
        ) {
            final var serviceProvider = Node.getInstance().getServiceProvider();
            return arguments.size() == 1 ? serviceProvider.findServices().stream()
                .map(CloudService::getName).toList() : EMPTY;
        }
    }
}
