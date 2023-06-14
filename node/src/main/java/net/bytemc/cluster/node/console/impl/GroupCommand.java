package net.bytemc.cluster.node.console.impl;

import java.util.Collection;
import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.CommandArgument;
import net.bytemc.cluster.api.command.annotations.SubCommand;
import net.bytemc.cluster.api.command.argument.transformers.BooleanArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.CloudGroupTypeTransformer;
import net.bytemc.cluster.api.command.argument.transformers.IntArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.StringArgumentTransformer;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.groups.CloudServiceGroupImpl;
import org.jetbrains.annotations.NotNull;

@Command(name = "group")
public final class GroupCommand {

    @SubCommand(name = "list", example = "group list")
    private void listGroups(CommandSender commandSender) {
        final Collection<CloudServiceGroup> groups = Node.getInstance().getServiceGroupProvider()
            .findGroups();
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


    @SubCommand(name = "create", example = "group create <name> <group_type> <memory> <fallback>")
    private void createGroup(
        CommandSender commandSender,
        @CommandArgument(name = "name", transformer = StringArgumentTransformer.class) String name,
        @CommandArgument(name = "group_type", transformer = CloudGroupTypeTransformer.class) CloudGroupType type,
        @CommandArgument(name = "memory", transformer = IntArgumentTransformer.class) int memory,
        @CommandArgument(name = "fallback", transformer = BooleanArgumentTransformer.class) boolean fallback
    ) {
        final CloudServiceGroupProvider cloudServiceGroupProvider = Node.getInstance()
            .getServiceGroupProvider();
        if (cloudServiceGroupProvider.exists(name)) {
            commandSender.sendMessage("Group with this name already exists");
            return;
        }

        cloudServiceGroupProvider.addGroup(
            new CloudServiceGroupImpl(name, type, 1, 1, memory, fallback));
        commandSender.sendMessage("Created group " + name);
    }

    @SubCommand(name = "remove", example = "group remove <name>")
    private void removeGroup(
        @NotNull CommandSender commandSender,
        @CommandArgument(name = "name", transformer = StringArgumentTransformer.class) String name
    ) {
        final CloudServiceGroupProvider cloudServiceGroupProvider = Node.getInstance()
            .getServiceGroupProvider();
        if (!cloudServiceGroupProvider.exists(name)) {
            commandSender.sendMessage("There is no group with this name");
            return;
        }

        cloudServiceGroupProvider.removeGroup(name);
        commandSender.sendMessage("Successfully removed group " + name);
    }

    @SubCommand(name = "info", example = "group info <name>")
    private void groupInfo(
        CommandSender commandSender,
        @CommandArgument(name = "name", transformer = StringArgumentTransformer.class) String name
    ) {
        final CloudServiceGroupProvider cloudServiceGroupProvider = Node.getInstance()
            .getServiceGroupProvider();
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
}