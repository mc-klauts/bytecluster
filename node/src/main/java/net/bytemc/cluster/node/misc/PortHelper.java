package net.bytemc.cluster.node.misc;

import net.bytemc.cluster.api.service.CloudServiceGroup;
import net.bytemc.cluster.node.Node;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import org.jetbrains.annotations.NotNull;

public final class PortHelper {

    //todo
    private static final int PORTS_BOUNCE_PROXY = 25565;
    //todo
    private static final int PORTS_BOUNCE = 25590;

    public static int getNextPort(@NotNull CloudServiceGroup service) {
        var port = service.getGroupType().isProxy() ? PORTS_BOUNCE_PROXY : PORTS_BOUNCE;
        while (isPortUsed(port)) {
            port++;
        }
        return port;
    }

    private static boolean isPortUsed(int port) {
        for (final var service : Node.getInstance().getServiceProvider().findServices()) {
            //todo
          //  if (service.getNode().equals(Base.getInstance().getNode().getName())) {
                if (service.getPort() == port) return true;
            //}
        }
        try (final var serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(port));
            return false;
        } catch (Exception exception) {
            return true;
        }
    }
}
