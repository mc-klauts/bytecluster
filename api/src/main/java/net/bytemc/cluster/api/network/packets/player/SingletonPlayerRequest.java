package net.bytemc.cluster.api.network.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Packet.Info(id = 33)
public final class SingletonPlayerRequest extends Packet {

    private Object identifier;

    @Override
    public void read(PacketBuffer reader) {
        var useUsername = reader.readBoolean();
        if (useUsername) {
            identifier = reader.readString();
        } else {
            identifier = reader.readUUID();
        }
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeBoolean(identifier instanceof String);
        if (identifier instanceof String) {
            writer.writeString((String) identifier);
        } else {
            writer.writeUUID((UUID) identifier);
        }
    }

}
