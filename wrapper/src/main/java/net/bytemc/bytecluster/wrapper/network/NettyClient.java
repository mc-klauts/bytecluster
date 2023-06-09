package net.bytemc.bytecluster.wrapper.network;

import io.netty5.bootstrap.Bootstrap;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelOption;
import io.netty5.channel.EventLoopGroup;
import io.netty5.channel.MultithreadEventLoopGroup;
import io.netty5.channel.epoll.Epoll;
import io.netty5.channel.epoll.EpollSocketChannel;
import io.netty5.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.misc.TaskFuture;
import net.bytemc.cluster.api.network.ConnectionUtils;
import net.bytemc.cluster.api.network.Packet;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public final class NettyClient {

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 8879;
    private static final int CONNECTION_TIMEOUT_MILLIS = 5_000;

    private final EventLoopGroup eventLoopGroup = new MultithreadEventLoopGroup(ConnectionUtils.newEventLoopGroup(0));
    private final String instanceName;

    @Nullable
    private Channel channel;

    public TaskFuture<Void> connect() {
        var result = new TaskFuture<Void>();
        new Bootstrap().group(this.eventLoopGroup)
                .channelFactory(Epoll.isAvailable() ? EpollSocketChannel::new : NioSocketChannel::new)
                .handler(new NettyNetworkClientInitializer(this, instanceName)
                        .option(ChannelOption.IP_TOS, 0x18)
                        .option(ChannelOption.AUTO_READ, true)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .option(ChannelOption.TCP_FASTOPEN_CONNECT, true)
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT_MILLIS))
                .connect(HOSTNAME, PORT)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        // ok, we connected successfully
                        result.complete(null);
                    } else {
                        // something went wrong
                        result.cancel(future.cause().getMessage());
                    }
                });
        return result;
    }

    public TaskFuture<Void> close() {
        var result = new TaskFuture<Void>();
        if (this.isConnected()) {
            assert this.channel != null;
            this.channel.close().addListener(future -> this.eventLoopGroup.shutdownGracefully(1L, 5L, TimeUnit.SECONDS).addListener(loop -> result.complete(null)));
        } else {
            this.eventLoopGroup.shutdownGracefully(1L, 5L, TimeUnit.SECONDS).addListener(future -> result.complete(null));
        }
        return result;
    }

    public void sendPacket(Packet packet) {
        if (isConnected()) {
            channel.writeAndFlush(packet);
        }
    }


    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isConnected() {
        return channel != null && channel.isActive();
    }

}
