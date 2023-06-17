package net.bytemc.bytecluster.wrapper.player;

import lombok.Setter;
import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerRequestKickPacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerSendMessagePacket;
import net.bytemc.cluster.api.network.packets.player.CloudPlayerTablistPacket;
import net.bytemc.cluster.api.player.AbstractCloudPlayer;
import net.bytemc.cluster.api.service.CloudService;

import java.util.UUID;

public final class WrapperCloudPlayer extends AbstractCloudPlayer {

    private final String currentProxyId;

    @Setter
    private String currentServerId;

    public WrapperCloudPlayer(String name, UUID uniqueId, String currentProxyId, String currentServerId) {
        super(name, uniqueId);

        this.currentProxyId = currentProxyId;
        this.currentServerId = currentServerId;
    }

    @Override
    public void sendMessage(String message) {
        Wrapper.getInstance().sendPacket(new CloudPlayerSendMessagePacket(getUniqueId(), message));
    }

    @Override
    public void kick(String reason) {
        Wrapper.getInstance().sendPacket(new CloudPlayerRequestKickPacket(getUniqueId(), reason));
    }

    @Override
    public void sendTablist(String header, String footer) {
        Wrapper.getInstance().sendPacket(new CloudPlayerTablistPacket(getUniqueId(), header, footer));
    }

    @Override
    public String getCurrentProxyId() {
        return this.currentProxyId;
    }

    @Override
    public AsyncTask<CloudService> getCurrentProxyAsync() {
        return Cluster.getInstance().getServiceProvider().findServiceAsync(this.currentProxyId);
    }

    @Override
    public CloudService getCurrentProxy() {
        return Cluster.getInstance().getServiceProvider().findService(this.currentProxyId);
    }

    @Override
    public String getCurrentServerId() {
        return this.currentServerId;
    }

    @Override
    public AsyncTask<CloudService> getCurrentServerAsync() {
        return Cluster.getInstance().getServiceProvider().findServiceAsync(this.currentProxyId);
    }

    @Override
    public CloudService getCurrentServer() {
        return Cluster.getInstance().getServiceProvider().findService(this.currentProxyId);
    }

    @Override
    public void sendToServer(String serverId) {
        //todo
    }
}
