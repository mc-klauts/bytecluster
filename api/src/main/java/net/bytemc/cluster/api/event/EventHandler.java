package net.bytemc.cluster.api.event;

import org.jetbrains.annotations.NotNull;

public interface EventHandler {

    <T> void registerListener(@NotNull Object clazz);

    void call(@NotNull Object event);

}
