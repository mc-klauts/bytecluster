package net.bytemc.cluster.api.player.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.Cluster;
import net.bytemc.cluster.api.event.AbstractCommunicatableEvent;
import net.bytemc.cluster.api.network.PacketBufferHelper;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.player.CloudPlayer;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class CloudPlayerSwitchEvent extends AbstractCommunicatableEvent {

    private CloudPlayer cloudPlayer;
    private String previousServer;
    private String currentServer;

    @Override
    public void read(PacketBuffer reader) {
        this.cloudPlayer = Cluster.getInstance().getPlayerHandler().getCloudPlayerFromBuffer(reader);
        this.previousServer = reader.readString();
        this.currentServer = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        PacketBufferHelper.writeCloudPlayer(writer, cloudPlayer);
        writer.writeString(previousServer);
        writer.writeString(currentServer);
    }
}
