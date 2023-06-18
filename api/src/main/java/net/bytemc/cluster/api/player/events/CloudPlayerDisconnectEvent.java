package net.bytemc.cluster.api.player.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.AbstractCommunicatableEvent;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.player.CloudPlayer;

@Getter
@AllArgsConstructor
public final class CloudPlayerDisconnectEvent extends AbstractCommunicatableEvent {

    private CloudPlayer cloudPlayer;

    @Override
    public void read(PacketBuffer reader) {
        this.cloudPlayer = Cluster.getInstance().getPlayerHandler().getCloudPlayerFromBuffer(reader);
    }

    @Override
    public void write(PacketBuffer writer) {
        PacketBufferHelper.writeCloudPlayer(writer, cloudPlayer);
    }
}
