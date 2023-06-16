package net.bytemc.cluster.api.player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CloudPlayerHandler {

    Collection<CloudPlayer> findPlayers();

    Optional<CloudPlayer> findPlayer(UUID uuid);

    Optional<CloudPlayer> findPlayer(String username);

}
