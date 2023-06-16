package net.bytemc.bytecluster.wrapper.player;

import net.bytemc.cluster.api.misc.async.AsyncTask;
import net.bytemc.cluster.api.player.CloudPlayer;
import net.bytemc.cluster.api.player.CloudPlayerHandler;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public final class WrapperCloudPlayerHandlerImpl implements CloudPlayerHandler {

    @Override
    public Collection<CloudPlayer> findPlayers() {
        //todo
        return null;
    }

    @Override
    public AsyncTask<CloudPlayer> findPlayersAsync() {
        //todo
        return null;
    }

    @Override
    public Optional<CloudPlayer> findPlayer(UUID uuid) {
        //todo
        return Optional.empty();
    }

    @Override
    public AsyncTask<Optional<CloudPlayer>> findPlayerAsync(UUID uuid) {
        //todo
        return null;
    }

    @Override
    public Optional<CloudPlayer> findPlayer(String username) {
        //todo
        return Optional.empty();
    }

    @Override
    public AsyncTask<Optional<CloudPlayer>> findPlayerAsync(String username) {
        //todo
        return null;
    }

    @Override
    public int getOnlineCount() {
        //todo
        return 0;
    }

    @Override
    public AsyncTask<Integer> getOnlineCountAsync() {
        //todo
        return null;
    }
}
