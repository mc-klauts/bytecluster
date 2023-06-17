package net.bytemc.cluster.node.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
public final class RuntimeConfiguration {

    public static final RuntimeConfiguration DEFAULT_CONFIGURATION = new RuntimeConfiguration(8879, 25565, 25900, "node-1");

    private int port;

    private int defaultProxyPort;
    private int defaultServerPort;

    private String runtimeNodeName;

}
