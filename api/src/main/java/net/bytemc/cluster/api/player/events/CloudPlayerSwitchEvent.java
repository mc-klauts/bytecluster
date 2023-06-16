package net.bytemc.cluster.api.player.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.event.AbstractCommunicatableEvent;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class CloudPlayerSwitchEvent extends AbstractCommunicatableEvent {

    private UUID uniqueId;
    private String previousServer;
    private String currentServer;

    @Override
    public void read(PacketBuffer reader) {
        this.uniqueId = reader.readUUID();
        this.previousServer = reader.readString();
        this.currentServer = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeUUID(uniqueId);
        writer.writeString(previousServer);
        writer.writeString(currentServer);
    }
}