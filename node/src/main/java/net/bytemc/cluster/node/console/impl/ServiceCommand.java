package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.CommandArgument;
import net.bytemc.cluster.api.command.annotations.SubCommand;
import net.bytemc.cluster.api.command.argument.transformers.StringArgumentTransformer;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.node.Node;

@Command(name = "service")
public final class ServiceCommand {

    @SubCommand(name = "shutdown", example = "service shutdown <name>")
    private void startServer(
            CommandSender commandSender,
            @CommandArgument(name = "name", transformer = StringArgumentTransformer.class) String name
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
}
