package net.bytemc.cluster.node.network.netty;

import io.netty5.channel.Channel;
import io.netty5.channel.ChannelInitializer;
import io.netty5.channel.ChannelOption;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class NettyOptionSettingChannelInitializer extends ChannelInitializer<Channel> {

    private final Map<ChannelOption<?>, Object> options = new LinkedHashMap<>();

    @Override
    protected void initChannel(Channel channel) {
        for (var optionEntry : this.options.entrySet()) {
            var option = (ChannelOption<Object>) optionEntry.getKey();
            if (channel.isOptionSupported(option)) {
                channel.setOption(option, optionEntry.getValue());
            }
        }
        // do further initialization to the channel
        this.doInitChannel(channel);
    }

    public @NonNull <T> NettyOptionSettingChannelInitializer option(@NonNull ChannelOption<T> option, @NonNull T value) {
        this.options.put(option, value);
        return this;
    }

    protected void doInitChannel(@NonNull Channel channel) {
    }
}
