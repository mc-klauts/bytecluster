package net.bytemc.cluster.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.misc.UnsafeAccess;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@Packet.Info(id = 20)
public final class CallEventPacket extends Packet {

    private AbstractCommunicatableEvent event;

    @Override
    public void write(@NotNull PacketBuffer buf) {
        buf.writeString(this.event.getClass().getName());
        event.write(buf);
    }

    @Override
    public void read(@NotNull PacketBuffer buf) {
        try {
            final var eventClass = Class.forName(buf.readString());
            event = (AbstractCommunicatableEvent) UnsafeAccess.allocate(eventClass);
            event.read(buf);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
