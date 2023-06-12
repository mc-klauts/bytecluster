package net.bytemc.cluster.node.console.impl;

import java.util.Collection;
import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.CommandArgument;
import net.bytemc.cluster.api.command.annotations.SubCommand;
import net.bytemc.cluster.api.command.argument.ArgumentTransformerType;
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
        @CommandArgument(name = "name", transformer = ArgumentTransformerType.STRING) String name,
        @CommandArgument(name = "group_type", transformer = ArgumentTransformerType.CLOUD_GROUP_TYPE) CloudGroupType type,
        @CommandArgument(name = "memory", transformer = ArgumentTransformerType.INT) int memory,
        @CommandArgument(name = "fallback", transformer = ArgumentTransformerType.BOOLEAN) boolean fallback
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
        @CommandArgument(name = "name", transformer = ArgumentTransformerType.STRING) String name
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
}