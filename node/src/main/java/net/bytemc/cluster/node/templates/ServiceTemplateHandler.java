package net.bytemc.cluster.node.templates;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.api.misc.FileHelper;
import net.bytemc.cluster.node.services.LocalCloudService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ServiceTemplateHandler {

    private static Path TEMPLATE_PATH = Path.of("templates");

    public ServiceTemplateHandler() {
        FileHelper.createDirectoryIfNotExists(TEMPLATE_PATH);

        this.createTemplate("GLOBAL");
        this.createTemplate("EVERY_SERVER");
        this.createTemplate("EVERY_PROXY");

        for (CloudServiceGroup group : Cluster.getInstance().getServiceGroupProvider().findGroups()) {
            createTemplate(group.getName());
        }
    }

    public void createTemplate(String id) {
        if (!isTemplateExists(id)) {
            try {
                Files.createDirectory(TEMPLATE_PATH.resolve(id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isTemplateExists(String id) {
        return Files.exists(TEMPLATE_PATH.resolve(id));
    }

    public void copyTemplate(String id, LocalCloudService service) {
        FileHelper.copyDirectory(TEMPLATE_PATH.resolve(id), service.getDirectory());
    }
}
