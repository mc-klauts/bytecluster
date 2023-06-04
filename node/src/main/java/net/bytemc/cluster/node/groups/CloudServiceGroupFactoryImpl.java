package net.bytemc.cluster.node.groups;

import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.api.service.CloudServiceGroupFactory;
import net.bytemc.cluster.node.configuration.ConfigurationHelper;
import net.bytemc.cluster.node.configuration.ConfigurationProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class CloudServiceGroupFactoryImpl implements CloudServiceGroupFactory {

    private static Path GROUPS_STORAGE_PATH = Path.of("groups");

    public CloudServiceGroupFactoryImpl() {
        ConfigurationHelper.createDirectoryIfNotExists(GROUPS_STORAGE_PATH);
    }

    @Override
    public List<CloudServiceGroup> loadGroups() {
        return Arrays.stream(GROUPS_STORAGE_PATH.toFile()
                        .listFiles())
                .map(it -> ConfigurationProvider.read(GROUPS_STORAGE_PATH.resolve(it.getName()), CloudServiceGroupImpl.class))
                .map(it -> (CloudServiceGroup) it)
                .toList();
    }

    @Override
    public boolean create(CloudServiceGroup cloudServiceGroup) {
        if(existInStorage(cloudServiceGroup)) {
            return false;
        }
        ConfigurationProvider.write(GROUPS_STORAGE_PATH.resolve(cloudServiceGroup.getName()), cloudServiceGroup);
        return true;
    }

    @Override
    public void remove(CloudServiceGroup group) {
        ConfigurationHelper.createDirectoryIfNotExists(GROUPS_STORAGE_PATH.resolve(group.getName()));
    }

    @Override
    public boolean existInStorage(CloudServiceGroup group) {
        return Files.exists(GROUPS_STORAGE_PATH.resolve(group.getName()));
    }
}
