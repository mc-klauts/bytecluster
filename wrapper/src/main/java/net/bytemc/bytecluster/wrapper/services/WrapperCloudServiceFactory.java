package net.bytemc.bytecluster.wrapper.services;

import net.bytemc.cluster.api.service.CloudService;
import net.bytemc.cluster.api.service.CloudServiceFactory;

public final class WrapperCloudServiceFactory implements CloudServiceFactory {

    @Override
    public void start(CloudService service) {
        //todo
    }

    @Override
    public void stop(CloudService service) {
        service.shutdown();
    }
}
