package net.bytemc.cluster.api.network;

import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.service.CloudService;

public final class PacketBufferHelper {

    public static void writeService(PacketBuffer packetBuffer, CloudService service) {
        packetBuffer.writeString(service.getName());
        packetBuffer.writeString(service.getGroupName());
        packetBuffer.writeString(service.getHostname());
        packetBuffer.writeString(service.getMotd());
        packetBuffer.writeInt(service.getMaxPlayers());
        packetBuffer.writeInt(service.getPort());
    }

}
