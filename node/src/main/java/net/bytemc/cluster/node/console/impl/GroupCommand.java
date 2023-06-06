package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.CloudGroupType;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.console.commands.CloudCommand;
import net.bytemc.cluster.node.groups.CloudServiceGroupImpl;
import net.bytemc.cluster.node.logger.Logger;

@CloudCommand.Command(name = "group")
public class GroupCommand extends CloudCommand {

    @Override
    public void execute(Node node, String[] args) {

        final var groupManager = node.getServiceGroupProvider();

        System.out.println(args.length);
        if (args.length == 5 && args[0].equalsIgnoreCase("create")) {

            var name = args[1];
            var type = CloudGroupType.valueOf(args[2].toUpperCase());
            var memory = Integer.valueOf(args[3]);
            var fallback = Boolean.parseBoolean(args[4]);

            Cluster.getInstance().getServiceGroupProvider().addGroup(new CloudServiceGroupImpl(name, type, 1, 1, memory, fallback));
            Logger.info("Successfully created group " + name + " with type " + type + ".");
            return;
        }

        Logger.info("group create <name> <type> <memory> <fallback>");

    }
}
