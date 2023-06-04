package net.bytemc.cluster.api.service;

public interface CloudServiceGroup {

    String getName();

    int getMinOnlineCount();

    int getMaxOnlineCount();

    int getMaxMemory();

    void shutdownAllServices();

    boolean isFallback();

}
