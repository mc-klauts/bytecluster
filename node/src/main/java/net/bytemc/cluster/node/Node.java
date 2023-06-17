package net.bytemc.cluster.node;

import lombok.Getter;
import lombok.Setter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.command.CommandExecutor;
import net.bytemc.cluster.api.command.CommandRepository;
import net.bytemc.cluster.api.event.EventHandler;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.node.cluster.ClusterNetwork;
import net.bytemc.cluster.node.configuration.ConfigurationHelper;
import net.bytemc.cluster.node.configuration.RuntimeConfiguration;
import net.bytemc.cluster.node.console.ConsoleTerminal;
import net.bytemc.cluster.node.console.impl.ClearScreenCommand;
import net.bytemc.cluster.node.console.impl.GroupCommand;
import net.bytemc.cluster.node.console.impl.ServiceCommand;
import net.bytemc.cluster.node.console.impl.ShutdownCommand;
import net.bytemc.cluster.node.dependency.DependencyHandler;
import net.bytemc.cluster.node.dependency.DependencyHandlerImpl;
import net.bytemc.cluster.node.event.CloudEventHandlerImpl;
import net.bytemc.cluster.node.groups.CloudServiceGroupProviderImpl;
import net.bytemc.cluster.node.logger.NodeLogger;
import net.bytemc.cluster.node.modules.CloudModuleHandler;
import net.bytemc.cluster.node.player.PlayerHandlerImpl;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;
import net.bytemc.cluster.node.templates.ServiceTemplateHandler;

import java.nio.file.Path;

@Getter
public final class Node extends Cluster {

    @Getter
    private static Node instance;

    @Setter
    private boolean running = true;

    private final Logger logger;

    private final RuntimeConfiguration runtimeConfiguration;
    private final DependencyHandler dependencyHandler;

    private final CloudServiceGroupProvider serviceGroupProvider;
    private final CloudServiceProvider serviceProvider;

    private final CommandExecutor commandExecutor;
    private final ConsoleTerminal consoleTerminal;
    private final ClusterNetwork clusterNetwork;

    private final EventHandler eventHandler;
    private final ServiceTemplateHandler templateHandler;
    private final PlayerHandlerImpl playerHandler;

    private final CloudModuleHandler moduleHandler;

    public Node() {
        instance = this;

        final CommandRepository commandRepository = Cluster.getInstance().getCommandRepository();
        commandRepository.registerCommand(ClearScreenCommand.class);
        commandRepository.registerCommand(ShutdownCommand.class);
        commandRepository.registerCommand(GroupCommand.class);
        commandRepository.registerCommand(ServiceCommand.class);

        this.dependencyHandler = new DependencyHandlerImpl();

        this.runtimeConfiguration = ConfigurationHelper.readConfiguration(Path.of("config.json"), RuntimeConfiguration.DEFAULT_CONFIGURATION);

        this.commandExecutor = new CommandExecutor();
        this.logger = new NodeLogger();

        this.eventHandler = new CloudEventHandlerImpl();
        this.consoleTerminal = new ConsoleTerminal();

        Logger.info("Initializing networkservice...");

        this.serviceGroupProvider = new CloudServiceGroupProviderImpl();
        this.playerHandler = new PlayerHandlerImpl();

        Logger.info("Loading following groups: " + String.join(", ", serviceGroupProvider.findGroups().stream().map(it -> it.getName()).toList()));

        this.templateHandler = new ServiceTemplateHandler();
        this.serviceProvider = new CloudServiceProviderImpl();
        this.clusterNetwork = new ClusterNetwork(this.runtimeConfiguration);

        this.moduleHandler = new CloudModuleHandler();

        // if user close the cluster without the shutdown command
        Runtime.getRuntime().addShutdownHook(new Thread(() -> NodeShutdownHandler.shutdown(this)));


        ((CloudServiceProviderImpl) this.serviceProvider).runProcess(this.serviceGroupProvider);
        ((CloudServiceProviderImpl) this.serviceProvider).queue();
    }
}
