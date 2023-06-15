package net.bytemc.cluster.api.network.packets.services;

import lombok.Getter;
import net.bytemc.cluster.api.network.Packet;
import net.bytemc.cluster.api.network.buffer.PacketBuffer;

import java.util.Optional;

@Packet.Info(id = 15)
public final class FindFallbackServiceResponse extends Packet {

    @Getter
    private Optional<String> fallbackId;

    public FindFallbackServiceResponse(Optional<String> fallbackId) {
        this.fallbackId = fallbackId;
    }

    @Override
    public void read(PacketBuffer reader) {
        this.fallbackId = reader.readBoolean() ? Optional.of(reader.readString()) : Optional.empty();
    }

    @Override
    public void write(PacketBuffer writer) {
        boolean present = fallbackId.isPresent();

        writer.writeBoolean(present);
        if (present) {
            writer.writeString(fallbackId.get());
        }
    }
}
