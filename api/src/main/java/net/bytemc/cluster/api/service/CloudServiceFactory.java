package net.bytemc.cluster.api.service;

public interface CloudServiceFactory {

    void start(CloudService service);

    void stop(CloudService service);

}
