package net.bytemc.cluster.node.player;

import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.player.CloudPlayer;
import net.bytemc.cluster.api.player.CloudPlayerHandler;

import java.util.*;

public final class PlayerHandlerImpl implements CloudPlayerHandler {

    private Map<UUID, CloudPlayer> cachedCloudPlayer = new HashMap<>();

    public PlayerHandlerImpl() {
        new PlayerPacketListener();
    }

    public void registerCloudPlayer(UUID uuid, String username, String server, String proxy) {
        this.cachedCloudPlayer.put(uuid, new LocalCloudPlayer(
                username,
                uuid,
                Cluster.getInstance().getServiceProvider().findService(proxy),
                Cluster.getInstance().getServiceProvider().findService(server))
        );
    }

    public void unregisterCloudPlayer(UUID uniqueId) {
        this.cachedCloudPlayer.remove(uniqueId);
    }

    @Override
    public Collection<CloudPlayer> findPlayers() {
        return cachedCloudPlayer.values();
    }

    @Override
    public AsyncTask<Collection<CloudPlayer>> findPlayersAsync() {
        return AsyncTask.completeWork(findPlayers());
    }

    @Override
    public Optional<CloudPlayer> findPlayer(UUID uuid) {
        return Optional.ofNullable(this.cachedCloudPlayer.get(cachedCloudPlayer));
    }

    @Override
    public AsyncTask<Optional<CloudPlayer>> findPlayerAsync(UUID uuid) {
        return AsyncTask.completeWork(findPlayer(uuid));
    }

    @Override
    public Optional<CloudPlayer> findPlayer(String username) {
        return cachedCloudPlayer.values().stream().filter(it -> it.getName().equals(username)).findAny();
    }

    @Override
    public AsyncTask<Optional<CloudPlayer>> findPlayerAsync(String username) {
        return AsyncTask.completeWork(findPlayer(username));
    }

    @Override
    public int getOnlineCount() {
        return this.cachedCloudPlayer.size();
    }

    @Override
    public AsyncTask<Integer> getOnlineCountAsync() {
        return AsyncTask.completeWork(getOnlineCount());
    }

    @Override
    public CloudPlayer getCloudPlayerFromBuffer(PacketBuffer buffer) {
        return new LocalCloudPlayer(
                buffer.readString(),
                buffer.readUUID(),
                Cluster.getInstance().getServiceProvider().findService(buffer.readString()),
                Cluster.getInstance().getServiceProvider().findService(buffer.readString())
        );
    }
}
