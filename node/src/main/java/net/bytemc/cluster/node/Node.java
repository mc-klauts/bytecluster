package net.bytemc.cluster.node;

import lombok.Getter;
import lombok.Setter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.EventHandler;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.node.cluster.ClusterNetwork;
import net.bytemc.cluster.node.configuration.ConfigurationHelper;
import net.bytemc.cluster.node.configuration.RuntimeConfiguration;
import net.bytemc.cluster.node.console.ConsoleTerminal;
import net.bytemc.cluster.node.console.commands.CommandHandler;
import net.bytemc.cluster.node.console.commands.SimpleCommandHandler;
import net.bytemc.cluster.node.event.CloudEventHandlerImpl;
import net.bytemc.cluster.node.groups.CloudServiceGroupProviderImpl;
import net.bytemc.cluster.node.logger.Logger;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;

import java.nio.file.Path;

@Getter
public final class Node extends Cluster {

    @Getter
    private static Node instance;

    @Setter
    private boolean running = true;

    private final RuntimeConfiguration runtimeConfiguration;
    private final CloudServiceGroupProvider serviceGroupProvider;
    private final CloudServiceProvider serviceProvider;

    private final CommandHandler commandHandler;
    private final ConsoleTerminal consoleTerminal;
    private final ClusterNetwork clusterNetwork;

    private EventHandler eventHandler;

    public Node() {
        instance = this;

        this.eventHandler = new CloudEventHandlerImpl();
        this.commandHandler = new SimpleCommandHandler();
        this.consoleTerminal = new ConsoleTerminal();

        Logger.info("Initializing networkservice...");

        this.runtimeConfiguration = ConfigurationHelper.readConfiguration(Path.of("config.json"), RuntimeConfiguration.DEFAULT_CONFIGURATION);
        this.serviceGroupProvider = new CloudServiceGroupProviderImpl();

        Logger.info("Loading following groups: " + String.join(", ", serviceGroupProvider.findGroups().stream().map(it -> it.getName()).toList()));

        this.serviceProvider = new CloudServiceProviderImpl(this.serviceGroupProvider);
        this.clusterNetwork = new ClusterNetwork(this.runtimeConfiguration);

        // if user close the cluster without the shutdown command
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NodeShutdownHandler.shutdown(this);
        }));

        ((CloudServiceProviderImpl) this.serviceProvider).queue();
    }
}
