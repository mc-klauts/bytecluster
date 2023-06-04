package net.bytemc.cluster.node.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class RuntimeConfiguraiton {

    public static RuntimeConfiguraiton DEFAULT_CONFIGURATION = new RuntimeConfiguraiton(8879);

    private int port;

}
