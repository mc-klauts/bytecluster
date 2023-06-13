package net.bytemc.cluster.api.event.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.event.CloudEvent;
import net.bytemc.cluster.api.service.CloudService;

@Getter
@AllArgsConstructor
public final class CloudServiceShutdownEvent implements CloudEvent {

    private final CloudService cloudService;

}
