package net.bytemc.cluster.node.player;

import lombok.Setter;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.player.AbstractCloudPlayer;
import net.bytemc.cluster.api.service.CloudService;

import java.util.UUID;

public final class LocalCloudPlayer extends AbstractCloudPlayer {

    private final CloudService currentProxy;

    @Setter
    private CloudService currentServer;

    public LocalCloudPlayer(String name, UUID uniqueId, CloudService proxy, CloudService service) {
        super(name, uniqueId);
        this.currentProxy = proxy;
        this.currentServer = service;
    }


    @Override
    public void sendMessage(String message) {
        //todo
    }

    @Override
    public void kick(String reason) {
        //todo
    }

    @Override
    public void sendTablist(String header, String footer) {
        //todo
    }

    @Override
    public String getCurrentProxyId() {
        return this.currentProxy.getName();
    }

    @Override
    public AsyncTask<CloudService> getCurrentProxyAsync() {
        return AsyncTask.completeWork(this.currentProxy);
    }

    @Override
    public CloudService getCurrentProxy() {
        return this.currentProxy;
    }

    @Override
    public String getCurrentServerId() {
        return this.currentServer.getName();
    }

    @Override
    public AsyncTask<CloudService> getCurrentServerAsync() {
        return AsyncTask.completeWork(this.currentProxy);
    }

    @Override
    public CloudService getCurrentServer() {
        return this.currentServer;
    }

    @Override
    public void sendToServer(String serverId) {

    }
}
