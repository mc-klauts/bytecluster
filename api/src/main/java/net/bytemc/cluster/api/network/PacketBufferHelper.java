package net.bytemc.cluster.api.network;

import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.player.CloudPlayer;
import net.bytemc.cluster.api.service.CloudService;

public final class PacketBufferHelper {

    public static void writeService(PacketBuffer packetBuffer, CloudService service) {
        packetBuffer.writeInt(service.getId());
        packetBuffer.writeString(service.getHostname());
        packetBuffer.writeString(service.getGroupName());
        packetBuffer.writeString(service.getMotd());
        packetBuffer.writeInt(service.getMaxPlayers());
        packetBuffer.writeInt(service.getPort());
        packetBuffer.writeEnum(service.getState());
    }

    public static void writeCloudPlayer(PacketBuffer packetBuffer, CloudPlayer cloudPlayer) {
        packetBuffer.writeString(cloudPlayer.getName());
        packetBuffer.writeUUID(cloudPlayer.getUniqueId());
        packetBuffer.writeString(cloudPlayer.getCurrentProxyId());
        packetBuffer.writeString(cloudPlayer.getCurrentServerId());
    }
}
