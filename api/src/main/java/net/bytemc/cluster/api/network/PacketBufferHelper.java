package net.bytemc.cluster.api.network;

import net.bytemc.cluster.api.network.buffer.PacketBuffer;
import net.bytemc.cluster.api.player.CloudPlayer;
import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceGroup;

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

    public static void writeCloudServiceGroup(PacketBuffer packetBuffer, CloudServiceGroup cloudServiceGroup) {
        packetBuffer.writeString(cloudServiceGroup.getName());
        packetBuffer.writeString(cloudServiceGroup.getBootstrapNodes());
        packetBuffer.writeEnum(cloudServiceGroup.getGroupType());
        packetBuffer.writeInt(cloudServiceGroup.getMinOnlineCount());
        packetBuffer.writeInt(cloudServiceGroup.getMaxOnlineCount());
        packetBuffer.writeInt(cloudServiceGroup.getMaxMemory());
        packetBuffer.writeInt(cloudServiceGroup.getDefaultStartPort());
        packetBuffer.writeBoolean(cloudServiceGroup.isFallback());
        packetBuffer.writeBoolean(cloudServiceGroup.isStaticService());
    }

}
