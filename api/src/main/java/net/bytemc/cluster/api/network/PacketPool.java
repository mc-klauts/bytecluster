package net.bytemc.cluster.api.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class PacketPool {

    private static final Map<Integer, Class<? extends Packet>> PACKET_ID_POOL = new HashMap<>();
    private final Map<UUID, Consumer<? extends Packet>> responsePool = new HashMap<>();

    public static void registerPacket(Class<? extends Packet> clazz) {
        PACKET_ID_POOL.put(clazz.getDeclaredAnnotation(Packet.Info.class).id(), clazz);
    }

    public static void registerPackets(Class<? extends Packet>... clazz) {
        for (var packetClazz : clazz) {
            registerPacket(packetClazz);
        }
    }

    public static Class<? extends Packet> getPacketClass(int id) {
        return PACKET_ID_POOL.get(id);
    }

    public void saveResponse(UUID uuid, Consumer<? extends Packet> applyAnswer) {
        this.responsePool.put(uuid, applyAnswer);
    }

    public boolean isResponsePresent(UUID responseId) {
        return this.responsePool.containsKey(responseId);
    }
}
