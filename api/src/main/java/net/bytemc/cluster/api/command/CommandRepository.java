package net.bytemc.cluster.api.command;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

public final class CommandRepository {

    @Getter
    private final Map<Class<?>, InternalCommand> commandMap = new HashMap<>();

    @SneakyThrows
    public void registerCommand(@NotNull Class<?> clazz) {
        final var clazzInstance = clazz.newInstance();
        final var command = new InternalCommand(clazzInstance);
        command.index();
        this.commandMap.put(clazz, command);
    }


    public void registerCommands(@NotNull Class<?>... clazz) {
        for (var aClass : clazz) {
            this.registerCommand(aClass);
        }
    }

    public void unregister(Class<?> clazz) {
        this.commandMap.remove(clazz);
    }

}
