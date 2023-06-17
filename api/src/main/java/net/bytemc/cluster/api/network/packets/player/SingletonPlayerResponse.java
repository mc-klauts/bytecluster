package net.bytemc.cluster.api.network.packets.player;

import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.player.CloudPlayer;

@Getter
@Packet.Info(id = 34)
public final class SingletonPlayerResponse extends Packet {

    private boolean foundPlayer;
    private CloudPlayer cloudPlayer;

    public SingletonPlayerResponse(CloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
        this.foundPlayer = (cloudPlayer != null);
    }

    @Override
    public void read(PacketBuffer reader) {
        this.foundPlayer = reader.readBoolean();

        if (this.foundPlayer) {
            this.cloudPlayer = Cluster.getInstance().getPlayerHandler().getCloudPlayerFromBuffer(reader);
        }
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeBoolean(foundPlayer);
        if (this.foundPlayer) {
            PacketBufferHelper.writeCloudPlayer(writer, cloudPlayer);
        }
    }
}
