package net.bytemc.cluster.api.service;

public interface CloudServiceGroup {

    String getName();

    CloudGroupType getGroupType();

    int getMinOnlineCount();

    int getMaxOnlineCount();

    int getMaxMemory();

    void shutdownAllServices();

    boolean isFallback();

    String getBootstrapNodes();

    int getDefaultStartPort();

    boolean isStaticService();

}
