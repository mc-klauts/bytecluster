package net.bytemc.cluster.api.statistics;

import net.bytemc.cluster.api.misc.async.AsyncTask;

public interface StatisticsHolder {

    double getCpuUsage();

    AsyncTask<Double> getCpuUsageAsync();

    int getMemory();

    AsyncTask<Integer> getMemoryAsync();

    long getBootTime();

}
