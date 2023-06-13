package net.bytemc.cluster.node.services;

import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.misc.FileHelper;
import net.bytemc.cluster.node.misc.PortHelper;

import java.util.*;

import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class CloudServiceFactoryQueue {

    private final CloudServiceProvider cloudServiceProvider;
    private final Queue<CloudServiceGroup> tasks = new LinkedList<>();

    public void addTask(CloudServiceGroup group, int amount) {
        for (var i = 0; i < amount; i++) {
            this.tasks.add(group);
        }
        Logger.info(amount + " service(s) from type " + group.getName() + " is now queued...");
    }

    public void start() {
        while (Node.getInstance().isRunning()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!tasks.isEmpty()) {
                var group = tasks.poll();

                // use localhost name, because we are running on the same machine
                var service = new LocalCloudService("127.0.0.1", group.getName(), "Default template motd", PortHelper.getNextPort(group), findId(group), 10);
                ((CloudServiceProviderImpl) Node.getInstance().getServiceProvider()).addService(service);
                cloudServiceProvider.getFactory().start(service);
            }
        }
    }

    public void shutdown() {
        FileHelper.deleteDirectory(Node.getInstance().getRuntimeConfiguration().getNodePath().getServerRunningPath());
    }

    private int findId(@NotNull CloudServiceGroup group) {
        var services = cloudServiceProvider.findServices(group.getName());
        var id = 1;
        while (isIdPresent(id, services)) {
            id++;
        }
        return id;
    }

    private boolean isIdPresent(int id, @NotNull Collection<CloudService> cloudServices) {
        return cloudServices.stream().anyMatch(service -> service.getId() == id);
    }
}