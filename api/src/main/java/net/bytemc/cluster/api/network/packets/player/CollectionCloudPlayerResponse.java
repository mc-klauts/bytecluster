package net.bytemc.cluster.api.network.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.player.CloudPlayer;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor
@Packet.Info(id = 38)
public final class CollectionCloudPlayerResponse extends Packet {

    private Collection<CloudPlayer> players;

    @Override
    public void read(PacketBuffer reader) {
        int length = reader.readInt();
        var playerPool = new ArrayList<CloudPlayer>();
        for (var i = 0; i < length; i++) {
            playerPool.add(Cluster.getInstance().getPlayerHandler().getCloudPlayerFromBuffer(reader));
        }
        this.players = playerPool;
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeInt(players.size());
        for (var player : this.players) {
            PacketBufferHelper.writeCloudPlayer(writer, player);
        }
    }
}
