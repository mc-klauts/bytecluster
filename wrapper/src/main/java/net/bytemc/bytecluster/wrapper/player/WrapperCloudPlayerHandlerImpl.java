package net.bytemc.bytecluster.wrapper.player;

import net.bytemc.bytecluster.wrapper.Wrapper;
import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.network.packets.player.*;
import net.bytemc.cluster.api.player.CloudPlayer;
import net.bytemc.cluster.api.player.CloudPlayerHandler;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public final class WrapperCloudPlayerHandlerImpl implements CloudPlayerHandler {

    @Override
    public Collection<CloudPlayer> findPlayers() {
        return this.findPlayersAsync().getSync(null);
    }

    @Override
    public AsyncTask<Collection<CloudPlayer>> findPlayersAsync() {
        var tasks = new AsyncTask<Collection<CloudPlayer>>();
        Wrapper.getInstance().sendQueryPacket(new CollectionCloudPlayerRequest(), CollectionCloudPlayerResponse.class, (packet) -> {
            tasks.complete(packet.getPlayers());
        });
        return tasks;
    }

    @Override
    public Optional<CloudPlayer> findPlayer(UUID uuid) {
        return findPlayerAsync(uuid).getSync(Optional.empty());
    }

    @Override
    public AsyncTask<Optional<CloudPlayer>> findPlayerAsync(UUID uuid) {
        var tasks = new AsyncTask<Optional<CloudPlayer>>();
        Wrapper.getInstance().sendQueryPacket(new SingletonPlayerRequest(uuid), SingletonPlayerResponse.class, (packet) -> {
            tasks.complete(Optional.ofNullable(packet.getCloudPlayer()));
        });
        return tasks;
    }

    @Override
    public Optional<CloudPlayer> findPlayer(String username) {
        return findPlayerAsync(username).getSync(Optional.empty());
    }

    @Override
    public AsyncTask<Optional<CloudPlayer>> findPlayerAsync(String username) {
        var tasks = new AsyncTask<Optional<CloudPlayer>>();
        Wrapper.getInstance().sendQueryPacket(new SingletonPlayerRequest(username), SingletonPlayerResponse.class, (packet) -> {
            tasks.complete(Optional.ofNullable(packet.getCloudPlayer()));
        });
        return tasks;
    }

    @Override
    public int getOnlineCount() {
        return getOnlineCountAsync().getSync(-1);
    }

    @Override
    public AsyncTask<Integer> getOnlineCountAsync() {
        var tasks = new AsyncTask<Integer>();
        Wrapper.getInstance().sendQueryPacket(new CloudPlayerAmountRequest(), CloudPlayerAmountResponse.class, (packet) -> tasks.complete(packet.getOnlineCount()));
        return tasks;
    }

    @Override
    public CloudPlayer getCloudPlayerFromBuffer(PacketBuffer buffer) {
        return new WrapperCloudPlayer(buffer.readString(), buffer.readUUID(), buffer.readString(), buffer.readString());
    }
}
