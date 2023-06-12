package net.bytemc.cluster.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

@Getter
@Packet.Info(id = 21)
@AllArgsConstructor
public final class SubscribeEventPacket extends Packet {

    private Class<? extends AbstractCommunicatableEvent> eventClass;

    @Override
    public void read(PacketBuffer reader) {
        try {
            this.eventClass = (Class<? extends AbstractCommunicatableEvent>) Class.forName(reader.readString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(PacketBuffer writer) {
        writer.writeString(eventClass.getName());
    }
}
