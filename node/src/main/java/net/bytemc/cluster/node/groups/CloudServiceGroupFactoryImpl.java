package net.bytemc.cluster.node.groups;

import java.util.Objects;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceGroupFactory;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.configuration.ConfigurationProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import net.bytemc.cluster.api.misc.FileHelper;
import org.jetbrains.annotations.NotNull;

public final class CloudServiceGroupFactoryImpl implements CloudServiceGroupFactory {

    //todo
    private static final Path GROUPS_STORAGE_PATH = Path.of("groups");

    public CloudServiceGroupFactoryImpl() {
        FileHelper.createDirectoryIfNotExists(GROUPS_STORAGE_PATH);
    }

    @Override
    public List<CloudServiceGroup> loadGroups() {
        return Arrays.stream(Objects.requireNonNull(GROUPS_STORAGE_PATH.toFile().listFiles()))
                .map(it -> ConfigurationProvider.read(GROUPS_STORAGE_PATH.resolve(it.getName()), CloudServiceGroupImpl.class))
                .map(cloudServiceGroup -> (CloudServiceGroup) cloudServiceGroup)
                .toList();
    }

    @Override
    public boolean create(CloudServiceGroup cloudServiceGroup) {
        if (existInStorage(cloudServiceGroup)) {
            return false;
        }
        ConfigurationProvider.write(GROUPS_STORAGE_PATH.resolve(cloudServiceGroup.getName()), cloudServiceGroup);

        //create template folder for new group
        Node.getInstance().getTemplateHandler().createTemplate(cloudServiceGroup.getName());
        return true;
    }

    @Override
    public void remove(@NotNull CloudServiceGroup group) {
        FileHelper.createDirectoryIfNotExists(GROUPS_STORAGE_PATH.resolve(group.getName()));
    }

    @Override
    public boolean existInStorage(@NotNull CloudServiceGroup group) {
        return Files.exists(GROUPS_STORAGE_PATH.resolve(group.getName()));
    }
}
