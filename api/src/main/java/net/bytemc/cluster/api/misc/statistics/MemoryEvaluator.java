package net.bytemc.cluster.api.misc.statistics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;

public final class MemoryEvaluator {

    private static final MemoryMXBean MEMORY_MX_BEAN = ManagementFactory.getMemoryMXBean();
    private static final RuntimeMXBean RUNTIME_MX_BEAN = ManagementFactory.getRuntimeMXBean();

    public static int getMax() {
        return (int) (MEMORY_MX_BEAN.getHeapMemoryUsage().getMax() / (1024 * 1024));
    }

    public static int getCurrent() {
        return (int) (MEMORY_MX_BEAN.getHeapMemoryUsage().getUsed() / (1024 * 1024));
    }

    public static String getCalcuationAsString() {
        return getCurrent() + "/" + getMax() + "MB";
    }
}
