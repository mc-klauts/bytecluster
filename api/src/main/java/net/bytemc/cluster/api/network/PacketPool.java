package net.bytemc.cluster.api.network;

import java.util.HashMap;
import java.util.Map;

public final class PacketPool {

    private static Map<Integer, Class<? extends Packet>> PACKET_ID_POOL = new HashMap<>();

    public void registerPacket(Class<? extends Packet> clazz) {
        PACKET_ID_POOL.put(clazz.getDeclaredAnnotation(Packet.Info.class).id(), clazz);
    }

    public static Class<? extends Packet> getPacketClass(int id) {
        return PACKET_ID_POOL.get(id);
    }

}
