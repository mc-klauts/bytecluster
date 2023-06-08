package net.bytemc.cluster.node.misc;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public final class CpuResolver {

    private static final OperatingSystemMXBean managementFactory = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static boolean isCpuOverloaded(double percentage) {
        return managementFactory.getCpuLoad() < percentage;
    }
}
