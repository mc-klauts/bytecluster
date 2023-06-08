package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.NodeShutdownHandler;
import net.bytemc.cluster.node.console.commands.CloudCommand;

@CloudCommand.Command(name = "stop", description = "Stops the cloudsystem", aliases = "exit")
public final class ShutdownCommand extends CloudCommand {

    @Override
    public void execute(Node node, String[] args) {
        NodeShutdownHandler.shutdown(Node.getInstance());
    }
}
