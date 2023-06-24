package net.bytemc.cluster.api.statistics;

public interface StatisticsHolder {

    double getCpuUsage();

    int getMemory();

    long getBootTime();

}
