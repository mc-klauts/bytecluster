package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.console.commands.CloudCommand;
import net.bytemc.cluster.node.groups.CloudServiceGroupImpl;
import net.bytemc.cluster.node.logger.Logger;
import org.jetbrains.annotations.NotNull;

@CloudCommand.Command(name = "group", aliases = {"groups"})
public final class GroupCommand extends CloudCommand {

    @Override
    public void execute(@NotNull Node node, String @NotNull [] args) {

        final var groupManager = node.getServiceGroupProvider();

        if(args.length == 1 && args[0].equalsIgnoreCase("list")){
            Logger.info("todo");
            return;
        }

        if (args.length == 5 && args[0].equalsIgnoreCase("create")) {

            var name = args[1];
            var type = CloudGroupType.valueOf(args[2].toUpperCase());
            var memory = Integer.valueOf(args[3]);
            var fallback = Boolean.parseBoolean(args[4]);

            groupManager.addGroup(new CloudServiceGroupImpl(name, type, 1, 1, memory, fallback));
            return;
        }

        Logger.info("group create <name> <type> <memory> <fallback>");

    }
}
