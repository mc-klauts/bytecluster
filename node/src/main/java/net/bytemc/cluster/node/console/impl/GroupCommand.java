package net.bytemc.cluster.node.console.impl;

import java.util.Arrays;
import java.util.List;
import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.CommandParameter;
import net.bytemc.cluster.api.command.annotations.SubCommand;
import net.bytemc.cluster.api.command.argument.transformers.BooleanArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.CloudGroupTypeTransformer;
import net.bytemc.cluster.api.command.argument.transformers.IntArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.StringArgumentTransformer;
import net.bytemc.cluster.api.command.autocompletion.TabCompleter;
import net.bytemc.cluster.api.command.commandsender.CommandSender;
import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.groups.CloudServiceGroupImpl;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;
import org.jetbrains.annotations.NotNull;

@Command(name = "group", aliases = {"template", "groups",
    "templates"})
public final class GroupCommand {

    @SubCommand(name = "start", tabCompleter = NamedGroupTabCompleter.class)
    private void startServer(
        CommandSender commandSender,
        @CommandParameter(name = "template", transformer = StringArgumentTransformer.class) String template
    ) {
        final var cloudServiceGroupProvider = Node.getInstance().getServiceGroupProvider();
        if (!cloudServiceGroupProvider.exists(template)) {
            commandSender.sendMessage("There is no group with this name");
            return;
        }

        ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider()).getQueue()
            .addTask(cloudServiceGroupProvider.findGroup(template), 1);
    }

    @SubCommand(name = "list")
    private void listGroups(CommandSender commandSender) {
        final var groups = Node.getInstance().getServiceGroupProvider().findGroups();
        if (groups.isEmpty()) {
            commandSender.sendMessage("No groups available. Try to create one.");
            commandSender.sendMessage("For more help use group create");
            return;
        }

        commandSender.sendMessage("List of all groups (" + groups.size() + ")");
        for (CloudServiceGroup group : groups) {
            commandSender.sendMessage("- " + group.getName());
        }
    }

    @SubCommand(name = "create", tabCompleter = GroupCreateTabCompleter.class)
    private void createGroup(
        CommandSender commandSender,
        @CommandParameter(name = "name", transformer = StringArgumentTransformer.class) String name,
        @CommandParameter(name = "group_type", transformer = CloudGroupTypeTransformer.class) CloudGroupType type,
        @CommandParameter(name = "memory", transformer = IntArgumentTransformer.class) int memory,
        @CommandParameter(name = "fallback", transformer = BooleanArgumentTransformer.class) boolean fallback
    ) {
        final var cloudServiceGroupProvider = Node.getInstance().getServiceGroupProvider();
        if (cloudServiceGroupProvider.exists(name)) {
            commandSender.sendMessage("Group with this name already exists");
            return;
        }

        cloudServiceGroupProvider.addGroup(
            new CloudServiceGroupImpl(name, type, 1, 1, memory, fallback, -1, "node-1", false));
        commandSender.sendMessage("Created group " + name);
    }

    @SubCommand(name = "remove", tabCompleter = NamedGroupTabCompleter.class)
    private void removeGroup(
        @NotNull CommandSender commandSender,
        @CommandParameter(name = "name", transformer = StringArgumentTransformer.class) String name
    ) {
        final var cloudServiceGroupProvider = Node.getInstance().getServiceGroupProvider();
        if (!cloudServiceGroupProvider.exists(name)) {
            commandSender.sendMessage("There is no group with this name");
            return;
        }

        cloudServiceGroupProvider.removeGroup(name);
        commandSender.sendMessage("Successfully removed group " + name);
    }

    @SubCommand(name = "info", tabCompleter = NamedGroupTabCompleter.class)
    private void groupInfo(
        CommandSender commandSender,
        @CommandParameter(name = "name", transformer = StringArgumentTransformer.class) String name
    ) {
        final var cloudServiceGroupProvider = Node.getInstance().getServiceGroupProvider();
        if (!cloudServiceGroupProvider.exists(name)) {
            commandSender.sendMessage("There is no group with this name");
            return;
        }

        final CloudServiceGroup group = cloudServiceGroupProvider.findGroup(name);
        commandSender.sendMessage("Information about group " + name);
        commandSender.sendMessage("- Name: " + group.getName());
        commandSender.sendMessage("- Type: " + group.getGroupType());
        commandSender.sendMessage("- Max. memory: " + group.getMaxMemory());
        commandSender.sendMessage("- Min. online count: " + group.getMinOnlineCount());
        commandSender.sendMessage("- Max. online count: " + group.getMaxOnlineCount());
        commandSender.sendMessage("- Fallback: " + group.isFallback());
    }

    public static class GroupCreateTabCompleter implements TabCompleter {

        @Override
        public List<String> complete(
            CommandSender commandSender,
            @NotNull List<String> arguments
        ) {
            return arguments.size() == 2 ? Arrays.stream(CloudGroupType.values()).map(Enum::name)
                .toList() : arguments.size() == 4 ? List.of("true", "false") : EMPTY;
        }
    }

    public static class NamedGroupTabCompleter implements TabCompleter {

        @Override
        public List<String> complete(
            CommandSender commandSender,
            @NotNull List<String> arguments
        ) {
            final var cloudServiceGroupProvider = Node.getInstance().getServiceGroupProvider();
            return arguments.size() == 1 ? cloudServiceGroupProvider.findGroups().stream()
                .map(CloudServiceGroup::getName).toList() : EMPTY;
        }
    }
}