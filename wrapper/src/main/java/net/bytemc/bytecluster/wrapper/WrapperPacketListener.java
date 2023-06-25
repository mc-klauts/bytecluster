package net.bytemc.bytecluster.wrapper;

import net.bytemc.cluster.api.misc.statistics.CpuEvaluator;
import net.bytemc.cluster.api.misc.statistics.MemoryEvaluator;
import net.bytemc.cluster.api.network.packets.services.CloudServiceCpuPacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceCpuRequestPacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceMemoryPacket;
import net.bytemc.cluster.api.network.packets.services.CloudServiceMemoryRequestPacket;

public final class WrapperPacketListener {

    public WrapperPacketListener() {
        var packetPool = Wrapper.getInstance().getPacketPool();

        packetPool.addQueryModification(CloudServiceMemoryRequestPacket.class, (packet) -> {
            if (!packet.getServiceId().equals(Wrapper.getInstance().getLocalService().getName())) {
                return new CloudServiceMemoryPacket(-1);
            }
            return new CloudServiceMemoryPacket(MemoryEvaluator.getCurrent());
        });

        packetPool.addQueryModification(CloudServiceCpuRequestPacket.class, (packet) -> {
            if (!packet.getServiceId().equals(Wrapper.getInstance().getLocalService().getName())) {
                return new CloudServiceCpuPacket(-1);
            }
            return new CloudServiceCpuPacket(CpuEvaluator.processCpuLoad());
        });

    }
}
