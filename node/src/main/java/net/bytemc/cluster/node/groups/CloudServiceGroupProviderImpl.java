package net.bytemc.cluster.node.groups;

import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceGroupFactory;
import net.bytemc.cluster.api.service.CloudServiceGroupProvider;
import net.bytemc.cluster.node.configuration.ConfigurationHelper;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.bytemc.cluster.api.misc.FileHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CloudServiceGroupProviderImpl implements CloudServiceGroupProvider {

    private final CloudServiceGroupFactory factory = new CloudServiceGroupFactoryImpl();
    private final Map<String, CloudServiceGroup> groups = new HashMap<>();

    public CloudServiceGroupProviderImpl() {
        factory.loadGroups().forEach(cloudServiceGroup -> groups.put(cloudServiceGroup.getName(), cloudServiceGroup));
    }

    @Contract(pure = true)
    @Override
    public @Nullable AsyncTask<Collection<CloudServiceGroup>> findGroupsAsync() {
        return AsyncTask.directly(groups.values());
    }

    @Contract(pure = true)
    @Override
    public @NotNull Collection<CloudServiceGroup> findGroups() {
        return groups.values();
    }

    @Contract(pure = true)
    @Override
    public @Nullable AsyncTask<CloudServiceGroup> findGroupAsync(String name) {
        return null;
    }

    @Contract(pure = true)
    @Override
    public CloudServiceGroup findGroup(String name) {
        return groups.get(name);
    }

    @Override
    public void addGroup(@NotNull CloudServiceGroup group) {

        if (this.exists(group.getName())) {
            Logger.warn("Group " + group.getName() + " already exists.");
            return;
        }

        //save in configuration
        ConfigurationHelper.createConfigurationIfNotExists(Path.of("groups", group.getName() + ".json"), group);
        Logger.info("Successfully created group " + group.getName() + ".");
    }

    @Override
    public void removeGroup(String name) {
        if (!this.exists(name)) {
            Logger.warn("Group " + name + " does not exist.");
            return;
        }
        FileHelper.deleteIfExists(Path.of("groups", name + ".json"));
        this.groups.remove(name);
        Logger.info("Successfully deleted group " + name + ".");
    }

    @Override
    public boolean exists(String id) {
        return this.groups.containsKey(id);
    }

    @Override
    public @NotNull AsyncTask<Boolean> existsAsync(String id) {
        return AsyncTask.directly(this.exists(id));
    }
}
