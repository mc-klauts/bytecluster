package net.bytemc.cluster.node.misc;

import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.node.Node;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

import net.bytemc.cluster.node.groups.CloudServiceGroupImpl;
import org.jetbrains.annotations.NotNull;

public final class PortHelper {


    public static int getNextPort(@NotNull CloudServiceGroup service) {
        var port = ((CloudServiceGroupImpl) service).getDefaultStartPort() == -1 ? (service.getGroupType().isProxy() ? Node.getInstance().getRuntimeConfiguration().getDefaultProxyPort() : Node.getInstance().getRuntimeConfiguration().getDefaultServerPort()) : ((CloudServiceGroupImpl) service).getDefaultStartPort();
        while (isPortUsed(port)) {
            port++;
        }
        return port;
    }

    private static boolean isPortUsed(int port) {
        var currentNodeName = Node.getInstance().getRuntimeConfiguration().getRuntimeNodeName();
        for (final var service : Node.getInstance().getServiceProvider().findServices()) {
            if (service.getGroup().getBootstrapNodes().equals(currentNodeName)) {
                if (service.getPort() == port) return true;
            }
        }
        try (final var serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(port));
            return false;
        } catch (Exception exception) {
            return true;
        }
    }
}
