package net.bytemc.cluster.api.network;

import io.netty5.channel.IoHandlerFactory;
import io.netty5.channel.epoll.Epoll;
import io.netty5.channel.epoll.EpollHandler;
import io.netty5.channel.nio.NioHandler;

public final class ConnectionUtils {

    public static IoHandlerFactory newEventLoopGroup(final int threads) {
        return (Epoll.isAvailable() ? EpollHandler.newFactory() : NioHandler.newFactory());
    }
}
