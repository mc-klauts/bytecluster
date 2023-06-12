package net.bytemc.cluster.api.network;

import io.netty5.channel.Channel;
import net.bytemc.cluster.api.misc.ListHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class PacketPool {

    private static final Map<Integer, Class<? extends Packet>> PACKET_ID_POOL = new HashMap<>();

    private final Map<UUID, Consumer<Packet>> responsePool = new HashMap<>();

    // default packet listeners
    private final Map<Class<? extends Packet>, List<BiConsumer<Channel, ? extends Packet>>> packetListeners = new HashMap<>();

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
        this.responsePool.put(uuid, (Consumer<Packet>) applyAnswer);
    }

    public <T extends Packet> void registerListener(Class<T> packet, BiConsumer<Channel, T> listener) {
        this.packetListeners.put(packet, ListHelper.getOrCreateAndElement(this.packetListeners.get(packet), listener));
    }

    public void callResponseRecievd(UUID id, Packet packet) {
        this.responsePool.get(id).accept(packet);
    }

    public boolean isResponsePresent(UUID responseId) {
        return this.responsePool.containsKey(responseId);
    }
}
