package net.bytemc.cluster.api.network.packets.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@AllArgsConstructor
@Packet.Info(id = 10)
public final class SingletonServiceRequest extends Packet {

    private String id;

    @Override
    public void read(PacketBuffer reader) {
        // only send pull request
        this.id = reader.readString();
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(id);
    }
}
