package net.bytemc.cluster.node.player;

import net.bytemc.cluster.api.player.CloudPlayer;
import net.bytemc.cluster.api.player.CloudPlayerHandler;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public final class PlayerHandlerImpl implements CloudPlayerHandler {

    public PlayerHandlerImpl() {
        new PlayerPacketListener();
    }

    @Override
    public Collection<CloudPlayer> findPlayers() {
        return null;
    }

    @Override
    public Optional<CloudPlayer> findPlayer(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<CloudPlayer> findPlayer(String username) {
        return Optional.empty();
    }
}
