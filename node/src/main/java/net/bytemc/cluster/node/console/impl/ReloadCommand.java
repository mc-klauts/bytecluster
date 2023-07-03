package net.bytemc.cluster.node.console.impl;

import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.DefaultExecution;
import net.bytemc.cluster.api.command.commandsender.CommandSender;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.groups.CloudServiceGroupProviderImpl;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;

@Command(name = "reload", aliases = {"rl"})
public final class ReloadCommand {

    @DefaultExecution
    public void handle(CommandSender commandSender) {
        var time = System.currentTimeMillis();
        Logger.info("Reloading network&8...");
        Logger.info("Trying to reload all group services&8...");
        ((CloudServiceGroupProviderImpl) Node.getInstance().getServiceGroupProvider()).reload();
        Logger.info("Successfully reloaded all group services&8!");
        Logger.info("Push the new services data to queue&8...");
        ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider()).getQueue()
            .checkQueue();
        Logger.info("Successfully pushed the new services data to queue&8!");
        Node.getInstance().getModuleHandler().unloadAllModules();
        Node.getInstance().getModuleHandler().loadModules();
        Logger.info("Successfully reloaded all modules&8!");
        Logger.info(
            "The reload was &asuccessful&8! &7Took " + (System.currentTimeMillis() - time) + "ms");
    }
}
