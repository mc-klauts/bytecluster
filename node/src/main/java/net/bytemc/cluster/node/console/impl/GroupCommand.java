package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.console.commands.CloudCommand;
import net.bytemc.cluster.node.groups.CloudServiceGroupImpl;

@CloudCommand.Command(name = "group")
public class GroupCommand extends CloudCommand {

    @Override
    public void execute(Node node, String[] args) {

        final var groupManager = node.getServiceGroupProvider();

        if (args.length == 4) {

            var name = args[0];

            Cluster.getInstance().getServiceGroupProvider().addGroup(new CloudServiceGroupImpl("",0, 0, 512, false));


        }

    }
}
