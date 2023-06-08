package net.bytemc.cluster.node;

import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.node.cluster.ClusterNetwork;
import net.bytemc.cluster.node.configuration.ConfigurationHelper;
import net.bytemc.cluster.node.configuration.RuntimeConfiguraiton;
import net.bytemc.cluster.node.console.ConsoleTerminal;
import net.bytemc.cluster.node.console.commands.CommandHandler;
import net.bytemc.cluster.node.console.commands.SimpleCommandHandler;
import net.bytemc.cluster.node.groups.CloudServiceGroupProviderImpl;
import net.bytemc.cluster.node.logger.Logger;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;

import java.nio.file.Path;

@Getter
public final class Node extends Cluster {

    @Getter
    private static Node instance;

    private final RuntimeConfiguraiton runtimeConfiguraiton;
    private final CloudServiceGroupProvider serviceGroupProvider;
    private final CloudServiceProvider serviceProvider;

    private final CommandHandler commandHandler;
    private final ConsoleTerminal consoleTerminal;
    private final ClusterNetwork clusterNetwork;

    public Node() {
        instance = this;

        this.commandHandler = new SimpleCommandHandler();
        this.consoleTerminal = new ConsoleTerminal();

        Logger.info("Initializing networkservice...");

        this.runtimeConfiguraiton = ConfigurationHelper.readConfiguration(Path.of("config.json"), RuntimeConfiguraiton.DEFAULT_CONFIGURATION);
        this.serviceGroupProvider = new CloudServiceGroupProviderImpl();

        Logger.info("Loading following groups: " + String.join(", ", serviceGroupProvider.findGroups().stream().map(it -> it.getName()).toList()));

        this.serviceProvider = new CloudServiceProviderImpl(this.serviceGroupProvider);
        this.clusterNetwork = new ClusterNetwork(this.runtimeConfiguraiton);

        // if user close the cluster without the shutdown command
        Runtime.getRuntime().addShutdownHook(new Thread(() -> NodeShutdownHandler.shutdown(this)));

        ((CloudServiceProviderImpl) this.serviceProvider).queue();
    }
}
