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
public final class CloudPlayerKickEvent extends AbstractCommunicatableEvent {

    private CloudPlayer cloudPlayer;
    private String reason;

    @Override
    public void read(PacketBuffer reader) {
        cloudPlayer = Cluster.getInstance().getPlayerHandler().getCloudPlayerFromBuffer(reader);
        reason = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        PacketBufferHelper.writeCloudPlayer(writer, cloudPlayer);
        writer.writeString(reason);
    }
}
