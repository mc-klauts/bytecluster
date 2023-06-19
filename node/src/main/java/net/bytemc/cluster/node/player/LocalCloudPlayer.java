package net.bytemc.cluster.node.player;

import lombok.Setter;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerRequestKickPacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerSendMessagePacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerSendServicePacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerTablistPacket;
import net.bytemc.cluster.api.player.AbstractCloudPlayer;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.services.CloudServiceProviderImpl;
import net.bytemc.cluster.node.services.LocalCloudService;

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
        ((LocalCloudService) getCurrentProxy()).sendPacket(new CloudPlayerSendMessagePacket(this.getUniqueId(), message));
    }

    @Override
    public void kick(String reason) {
        ((LocalCloudService) getCurrentProxy()).sendPacket(new CloudPlayerRequestKickPacket(this.getUniqueId(), reason));
    }

    @Override
    public void sendTablist(String header, String footer) {
        ((LocalCloudService) getCurrentProxy()).sendPacket(new CloudPlayerTablistPacket(this.getUniqueId(), header, footer));
    }

    @Override
    public String getCurrentProxyId() {
        return this.currentProxy.getName();
    }

    @Override
    public AsyncTask<CloudService> getCurrentProxyAsync() {
        return AsyncTask.directly(this.currentProxy);
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
        return AsyncTask.directly(this.currentProxy);
    }

    @Override
    public CloudService getCurrentServer() {
        return this.currentServer;
    }

    @Override
    public void sendToServer(String serverId) {
        ((LocalCloudService) getCurrentProxy()).sendPacket(new CloudPlayerSendServicePacket(getUniqueId(), serverId));
    }
}
