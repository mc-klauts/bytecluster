package net.bytemc.cluster.api.service;

public interface CloudService {

    String name();

    String host();

    int port();

    String group();

    int onlinePlayers();

    int maxPlayers();

    String motd();

    void shutdown();

}
