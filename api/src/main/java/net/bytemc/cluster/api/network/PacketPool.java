package net.bytemc.cluster.api.network;

import io.netty5.channel.Channel;
import net.bytemc.cluster.api.event.CallEventPacket;
import net.bytemc.cluster.api.event.SubscribeEventPacket;
import net.bytemc.cluster.api.misc.concurrent.ListHelper;
import net.bytemc.cluster.api.network.packets.ServiceIdentifiyPacket;
import net.bytemc.cluster.api.network.packets.player.*;
import net.bytemc.cluster.api.network.packets.services.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class PacketPool {

    private static final Map<Integer, Class<? extends Packet>> PACKET_ID_POOL = new HashMap<>();

    static {
        PacketPool.registerPackets(
                // general packets
                QueryPacket.class,

                // cloud logic packets
                ServiceIdentifiyPacket.class,

                // api packets
                SingletonServiceResponse.class,
                SingletonServiceRequest.class,

                CollectionServiceRequest.class,
                CollectionServiceResponse.class,

                FindFallbackServiceRequest.class,
                FindFallbackServiceResponse.class,

                // player packets
                CloudPlayerConnectPacket.class,
                CloudPlayerDisconnectPacket.class,
                CloudPlayerSwitchPacket.class,
                SingletonPlayerRequest.class,
                SingletonPlayerResponse.class,s

                // event system basics
                CallEventPacket.class,
                SubscribeEventPacket.class
        );
    }

    private final Map<UUID, Consumer<Packet>> responsePool = new HashMap<>();

    // default packet listeners
    private final Map<Class<? extends Packet>, List<BiConsumer<Channel, Packet>>> packetListeners = new HashMap<>();

    // if a packet is sent to a service, it can be modified before it is sent, if the service is not the target
    private final Map<Class<? extends Packet>, Function<Packet, Packet>> queryResponseModificationPool = new HashMap<>();

    public static void registerPacket(Class<? extends Packet> clazz) {
        PACKET_ID_POOL.put(clazz.getDeclaredAnnotation(Packet.Info.class).id(), clazz);
    }

    public static void registerPackets(Class<? extends Packet> @NotNull ... clazz) {
        for (var packetClazz : clazz) {
            registerPacket(packetClazz);
        }
    }

    public static Class<? extends Packet> getPacketClass(int id) {
        return PACKET_ID_POOL.get(id);
    }

    public <T extends Packet> void addQueryModification(Class<T> clazz, Function<T, ? extends Packet> modification) {
        this.queryResponseModificationPool.put(clazz, (Function<Packet, Packet>) modification);
    }

    public <T extends Packet> void addQueryModification(Class<T> clazz, Consumer<T> modification) {
        this.queryResponseModificationPool.put(clazz, packet -> {
            modification.accept((T) packet);
            return packet;
        });
    }


    public void saveResponse(UUID uuid, Consumer<? extends Packet> applyAnswer) {
        this.responsePool.put(uuid, (Consumer<Packet>) applyAnswer);
    }

    public <T extends Packet> void registerListener(Class<T> packet, BiConsumer<Channel, T> listener) {
        this.packetListeners.put(packet, ListHelper.addElementInList(this.packetListeners.getOrDefault(packet, new ArrayList<>()), (BiConsumer<Channel, Packet>) listener));
    }

    public boolean isQueryModificationPresent(Class<? extends Packet> packetClass) {
        return this.queryResponseModificationPool.containsKey(packetClass);
    }

    public <T extends Packet> Packet callQueryModification(@NotNull Packet packet) {
        return this.queryResponseModificationPool.get(packet.getClass()).apply(packet);
    }

    public void callResponseRecievd(UUID id, Packet packet) {
        this.responsePool.get(id).accept(packet);
        this.responsePool.remove(id);
    }

    public boolean isResponsePresent(UUID responseId) {
        return this.responsePool.containsKey(responseId);
    }

    public void callPacketListener(Channel channel, Packet packet) {
        if(!packetListeners.containsKey(packet.getClass())) {
            return;
        }
        packetListeners.get(packet.getClass()).forEach(listener -> listener.accept(channel, packet));
    }

}
