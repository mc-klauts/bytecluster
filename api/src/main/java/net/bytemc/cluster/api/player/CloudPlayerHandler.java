package net.bytemc.cluster.api.player;

import net.bytemc.cluster.api.misc.async.AsyncTask;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CloudPlayerHandler {

    Collection<CloudPlayer> findPlayers();

    AsyncTask<CloudPlayer> findPlayersAsync();

    Optional<CloudPlayer> findPlayer(UUID uuid);

    AsyncTask<Optional<CloudPlayer>> findPlayerAsync(UUID uuid);

    Optional<CloudPlayer> findPlayer(String username);

    AsyncTask<Optional<CloudPlayer>> findPlayerAsync(String username);

    int getOnlineCount();

    AsyncTask<Integer> getOnlineCountAsync();

}
