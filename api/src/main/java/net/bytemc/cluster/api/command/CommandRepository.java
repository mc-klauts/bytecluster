package net.bytemc.cluster.api.command;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

public final class CommandRepository {

    @Getter private final Map<Class<?>, InternalCommand> commandMap = new HashMap<>();

    @SneakyThrows
    public void registerCommand(@NotNull Class<?> clazz) {
        final Object clazzInstance = clazz.newInstance();
        final InternalCommand command = new InternalCommand(clazzInstance);

        command.initialize();
        this.commandMap.put(clazz, command);
    }

    public void unregister(Class<?> clazz) {
        this.commandMap.remove(clazz);
    }

}
