package net.bytemc.cluster.node.templates;

import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.misc.FileHelper;

public final class ServiceTemplateHandler {

    public ServiceTemplateHandler() {
        FileHelper.createDirectoryIfNotExists(Node.getInstance().getRuntimeConfiguration().getNodePath().getTemplatePath());
    }



}
