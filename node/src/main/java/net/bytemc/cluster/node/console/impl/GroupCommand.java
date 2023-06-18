package net.bytemc.cluster.node.console.impl;

import java.util.Arrays;
import java.util.List;
import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.CommandArgument;
import net.bytemc.cluster.api.command.annotations.SubCommand;
import net.bytemc.cluster.api.command.argument.transformers.BooleanArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.CloudGroupTypeTransformer;
import net.bytemc.cluster.api.command.argument.transformers.IntArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.StringArgumentTransformer;
import net.bytemc.cluster.api.command.autocompletion.TabCompleter;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.console.impl.GroupCommand.GroupTabCompleter;
import net.bytemc.cluster.node.groups.CloudServiceGroupImpl;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;
import org.jetbrains.annotations.NotNull;

@Command(name = "group", aliases = {"template", "groups",
    "templates"}, tabCompleter = GroupTabCompleter.class)
public final class GroupCommand {

    @SubCommand(name = "start", example = "group start <name>", tabCompleter = NamedGroupTabCompleter.class)
    private void startServer(
        CommandSender commandSender,
        @CommandArgument(name = "template", transformer = StringArgumentTransformer.class) String template
    ) {
        final var cloudServiceGroupProvider = Node.getInstance().getServiceGroupProvider();
        if (!cloudServiceGroupProvider.exists(template)) {
            commandSender.sendMessage("There is no group with this name");
            return;
        }

        ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider()).getQueue()
            .addTask(cloudServiceGroupProvider.findGroup(template), 1);
    }

    @SubCommand(name = "list", example = "group list")
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

    @SubCommand(name = "create", example = "group create <name> <group_type> <memory> <fallback>", tabCompleter = GroupCreateTabCompleter.class)
    private void createGroup(
        CommandSender commandSender,
        @CommandArgument(name = "name", transformer = StringArgumentTransformer.class) String name,
        @CommandArgument(name = "group_type", transformer = CloudGroupTypeTransformer.class) CloudGroupType type,
        @CommandArgument(name = "memory", transformer = IntArgumentTransformer.class) int memory,
        @CommandArgument(name = "fallback", transformer = BooleanArgumentTransformer.class) boolean fallback
    ) {
        final var cloudServiceGroupProvider = Node.getInstance().getServiceGroupProvider();
        if (cloudServiceGroupProvider.exists(name)) {
            commandSender.sendMessage("Group with this name already exists");
            return;
        }

        cloudServiceGroupProvider.addGroup(new CloudServiceGroupImpl(name, type, 1, 1, memory, fallback, -1, "node-1"));
        commandSender.sendMessage("Created group " + name);
    }

    @SubCommand(name = "remove", example = "group remove <name>", tabCompleter = NamedGroupTabCompleter.class)
    private void removeGroup(
        @NotNull CommandSender commandSender,
        @CommandArgument(name = "name", transformer = StringArgumentTransformer.class) String name
    ) {
        final var cloudServiceGroupProvider = Node.getInstance().getServiceGroupProvider();
        if (!cloudServiceGroupProvider.exists(name)) {
            commandSender.sendMessage("There is no group with this name");
            return;
        }

        cloudServiceGroupProvider.removeGroup(name);
        commandSender.sendMessage("Successfully removed group " + name);
    }

    @SubCommand(name = "info", example = "group info <name>", tabCompleter = NamedGroupTabCompleter.class)
    private void groupInfo(
        CommandSender commandSender,
        @CommandArgument(name = "name", transformer = StringArgumentTransformer.class) String name
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

    public static class GroupTabCompleter implements TabCompleter {

        @Override
        public List<String> complete(
            CommandSender commandSender,
            String name,
            String[] args
        ) {
            return List.of("list", "create", "remove", "start");
        }
    }

    public static class GroupCreateTabCompleter implements TabCompleter {

        @Override
        public List<String> complete(
            CommandSender commandSender,
            String name,
            String @NotNull [] args
        ) {
            return args.length == 3 ? Arrays.stream(CloudGroupType.values()).map(Enum::name)
                .toList() : args.length == 5 ? List.of("true", "false") : EMPTY;
        }
    }

    public static class NamedGroupTabCompleter implements TabCompleter {

        @Override
        public List<String> complete(
            CommandSender commandSender,
            String name,
            String @NotNull [] args
        ) {
            final var cloudServiceGroupProvider = Node.getInstance().getServiceGroupProvider();
            return args.length == 2 ? cloudServiceGroupProvider.findGroups().stream()
                .map(CloudServiceGroup::getName).toList() : EMPTY;
        }
    }
}