package net.bytemc.cluster.api.event.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.cluster.api.event.CloudEvent;

@Getter
@AllArgsConstructor
public final class CloudServiceConnectEvent implements CloudEvent {

    private final String serviceName;

}
