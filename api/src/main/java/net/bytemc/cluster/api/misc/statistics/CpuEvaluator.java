package net.bytemc.cluster.api.misc.statistics;

import org.jetbrains.annotations.Range;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public final class CpuEvaluator {

    private static final OperatingSystemMXBean OS_BEAN = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public static @Range(from = -1, to = 100) double processCpuLoad() {
        return convertToPercentage(OS_BEAN.getProcessCpuLoad());
    }

    private static double convertToPercentage(double value) {
        if (value == 0) {
            return 0;
        } else if (value < 0) {
            return -1;
        } else {
            return Math.min(100D, value * 100D);
        }
    }

}
