package net.bytemc.cluster.api.player;

import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.service.CloudService;

public interface CloudPlayer {

    String getName();

    String getUniqueID();

    void sendMessage(String message);

    void kick(String reason);

    void sendTablist(String header, String footer);

    String getCurrentProxyId();

    AsyncTask<CloudService> getCurrentProxyAsync();

    CloudService getCurrentProxy();

    String getCurrentServerId();

    AsyncTask<CloudService> getCurrentServerAsync();

    CloudService getCurrentServer();

    void sendToServer(String serverId);

    void sendToServer(CloudService service);

}

