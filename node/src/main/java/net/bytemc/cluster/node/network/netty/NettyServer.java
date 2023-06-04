package net.bytemc.cluster.node.network.netty;

import io.netty5.bootstrap.ServerBootstrap;
import io.netty5.channel.ChannelOption;
import io.netty5.channel.EventLoopGroup;
import io.netty5.channel.MultithreadEventLoopGroup;
import io.netty5.channel.epoll.Epoll;
import io.netty5.channel.epoll.EpollServerSocketChannel;
import io.netty5.channel.socket.nio.NioServerSocketChannel;
import io.netty5.channel.unix.UnixChannelOption;
import io.netty5.util.concurrent.Future;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.network.ConnectionUtils;
import net.bytemc.cluster.node.logger.Logger;
import org.jetbrains.annotations.Nullable;

public final class NettyServer {

    private static final String HOSTNAME = "0.0.0.0";
    private final EventLoopGroup bossEventLoopGroup = new MultithreadEventLoopGroup(ConnectionUtils.newEventLoopGroup(1));
    private final EventLoopGroup workerEventLoopGroup = new MultithreadEventLoopGroup(ConnectionUtils.newEventLoopGroup(0));

    @Nullable
    private Future<Void> channelFuture;

    public TaskFuture<Void> initialize(int port) {
        var task = new TaskFuture<Void>();
        new ServerBootstrap()
                .channelFactory(Epoll.isAvailable() ? EpollServerSocketChannel::new : NioServerSocketChannel::new)
                .group(this.bossEventLoopGroup, this.workerEventLoopGroup)
                .handler(new NettyOptionSettingChannelInitializer()
                        .option(ChannelOption.TCP_FASTOPEN, 3)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .option(UnixChannelOption.SO_REUSEPORT, true))

                .childHandler(new NettyNetworkServerInitializer()
                        .option(ChannelOption.IP_TOS, 0x18)
                        .option(ChannelOption.AUTO_READ, true)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .option(ChannelOption.SO_KEEPALIVE, true))

                .bind(HOSTNAME, port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        // ok, we bound successfully
                        task.complete(null);
                        channelFuture = future.getNow().closeFuture();
                    } else {
                        // something went wrong
                        task.cancel(future.cause().getMessage());
                    }
                });
        return task;
    }

    public void close() {
        this.channelFuture.cancel();

        this.bossEventLoopGroup.shutdownGracefully();
        this.workerEventLoopGroup.shutdownGracefully();

        Logger.info("NettyServer closed and shutdown gracefully.");
    }
}
