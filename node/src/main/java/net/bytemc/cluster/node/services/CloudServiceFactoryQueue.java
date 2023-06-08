package net.bytemc.cluster.node.services;

import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceProvider;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.misc.PortHelper;

import java.util.*;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class CloudServiceFactoryQueue {

    private final CloudServiceProvider cloudServiceProvider;
    private final Queue<CloudServiceGroup> tasks = new LinkedList<>();

    public void addTask(CloudServiceGroup group) {
        this.tasks.add(group);
    }

    public void start() {
        while (Node.getInstance().isRunning()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!tasks.isEmpty()) {
                var group = tasks.poll();
                var service = new LocalCloudService(group.getName(), group.getName(), findId(group), PortHelper.getNextPort(group), 10, "Default template motd");
                cloudServiceProvider.getFactory().start(service);
            }
        }
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