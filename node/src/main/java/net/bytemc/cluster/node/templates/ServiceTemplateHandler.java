package net.bytemc.cluster.node.templates;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.api.misc.FileHelper;
import net.bytemc.cluster.node.services.LocalCloudService;

import java.io.IOException;
import java.nio.file.Files;

public final class ServiceTemplateHandler {

    public ServiceTemplateHandler() {
        FileHelper.createDirectoryIfNotExists(Node.getInstance().getRuntimeConfiguration().getNodePath().getTemplatePath());

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
                Files.createDirectory(Node.getInstance().getRuntimeConfiguration().getNodePath().getTemplatePath().resolve(id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isTemplateExists(String id) {
        return Files.exists(Node.getInstance().getRuntimeConfiguration().getNodePath().getTemplatePath().resolve(id));
    }

    public void copyTemplate(String id, LocalCloudService service) {
        FileHelper.copyDirectory(Node.getInstance().getRuntimeConfiguration().getNodePath().getTemplatePath().resolve(id), service.getDirectory());
    }
}
