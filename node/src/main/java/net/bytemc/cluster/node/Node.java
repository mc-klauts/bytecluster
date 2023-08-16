package net.bytemc.cluster.node;

import java.nio.file.Path;
import lombok.Getter;
import lombok.Setter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.EventHandler;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.properties.GlobalPropertyHandler;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.node.cluster.ClusterNetwork;
import net.bytemc.cluster.node.configuration.ConfigurationHelper;
import net.bytemc.cluster.node.configuration.RuntimeConfiguration;
import net.bytemc.cluster.node.console.ConsoleTerminal;
import net.bytemc.cluster.node.console.impl.ClearScreenCommand;
import net.bytemc.cluster.node.console.impl.GroupCommand;
import net.bytemc.cluster.node.console.impl.ReloadCommand;
import net.bytemc.cluster.node.console.impl.ServiceCommand;
import net.bytemc.cluster.node.console.impl.ShutdownCommand;
import net.bytemc.cluster.node.dependency.DependencyHandler;
import net.bytemc.cluster.node.dependency.DependencyHandlerImpl;
import net.bytemc.cluster.node.event.CloudEventHandlerImpl;
import net.bytemc.cluster.node.groups.CloudServiceGroupProviderImpl;
import net.bytemc.cluster.node.logger.NodeLogger;
import net.bytemc.cluster.node.logger.NodeOutputPrintStream;
import net.bytemc.cluster.node.modules.CloudModuleHandler;
import net.bytemc.cluster.node.player.PlayerHandlerImpl;
import net.bytemc.cluster.node.properties.GlobalPropertyHandlerImpl;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;
import net.bytemc.cluster.node.templates.ServiceTemplateHandler;
import org.jline.utils.Log;

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

    private final ConsoleTerminal consoleTerminal;
    private final ClusterNetwork clusterNetwork;

    private final EventHandler eventHandler;
    private final ServiceTemplateHandler templateHandler;
    private final PlayerHandlerImpl playerHandler;

    private final CloudModuleHandler moduleHandler;
    private final GlobalPropertyHandler globalPropertyHandler;

    public Node() {
        instance = this;

        this.dependencyHandler = new DependencyHandlerImpl();
        this.consoleTerminal = new ConsoleTerminal();

        System.setErr(new NodeOutputPrintStream());
        System.setOut(new NodeOutputPrintStream());

        this.logger = new NodeLogger();

        final var commandRepository = Cluster.getInstance().getCommandRepository();
        commandRepository.registerCommands(ClearScreenCommand.class, ShutdownCommand.class,
            GroupCommand.class, ServiceCommand.class, ReloadCommand.class);

        this.runtimeConfiguration = ConfigurationHelper.readConfiguration(Path.of("config.json"),
            RuntimeConfiguration.DEFAULT_CONFIGURATION);

        this.globalPropertyHandler = new GlobalPropertyHandlerImpl();

        this.moduleHandler = new CloudModuleHandler();
        this.eventHandler = new CloudEventHandlerImpl();

        Logger.empty("  ʙʏᴛᴇᴄʟᴜsᴛᴇʀ &8- &7©ʙʏᴛᴇᴍᴄɴᴇᴛᴢᴡᴇʀᴋ");
        Logger.empty(" ");

        this.serviceGroupProvider = new CloudServiceGroupProviderImpl();
        this.playerHandler = new PlayerHandlerImpl();

        Logger.info("Loading following groups: " + String.join(", ",
            serviceGroupProvider.findGroups().stream().map(it -> it.getName()).toList()));

        this.moduleHandler.loadModules();

        this.templateHandler = new ServiceTemplateHandler();
        this.serviceProvider = new CloudServiceProviderImpl();
        this.clusterNetwork = new ClusterNetwork(this.runtimeConfiguration);

        // if user close the cluster without the shutdown command
        Runtime.getRuntime().addShutdownHook(new Thread(() -> NodeShutdownHandler.shutdown(this)));

        ((CloudServiceProviderImpl) this.serviceProvider).runProcess(this.serviceGroupProvider);
        ((CloudServiceProviderImpl) this.serviceProvider).queue();
    }

    @Override
    public ClassLoader getRuntimeClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }
}
