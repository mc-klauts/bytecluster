package net.bytemc.bytecluster.wrapper;

import net.bytemc.cluster.api.misc.statistics.MemoryEvaluator;
import net.bytemc.cluster.api.network.packets.services.CloudServiceMemoryPacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceMemoryRequestPacket;

public final class WrapperPacketListener {

    public WrapperPacketListener() {
        Wrapper.getInstance().getPacketPool().addQueryModification(CloudServiceMemoryRequestPacket.class, (packet) -> {
            if (!packet.getServiceId().equals(Wrapper.getInstance().getLocalService().getName())) {
                return new CloudServiceMemoryPacket(-1);
            }
            return new CloudServiceMemoryPacket(MemoryEvaluator.getCurrent());
        });
    }
}
