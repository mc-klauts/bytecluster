package net.bytemc.cluster.api.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.Setter;
import net.bytemc.cluster.api.command.commandsender.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandRepository {

    private final Map<Class<?>, IndexedCommand> commandMap = new HashMap<>();

    private Consumer<IndexedCommand> onCommandRegister;

    public void registerCommand(Class<?> commandClass) {
        final CommandCreator commandCreator = new CommandCreator(commandClass);
        commandCreator.handle();
        IndexedCommand indexedCommand = commandCreator.getIndexedCommand();

        this.commandMap.put(commandClass, indexedCommand);

        if (this.onCommandRegister != null) {
            this.onCommandRegister.accept(indexedCommand);
        }
    }

    public void unregisterCommand(Class<?> commandClass) {
        this.commandMap.remove(commandClass);
    }

    @Contract(pure = true)
    @Unmodifiable
    public @NotNull Collection<IndexedCommand> findAll() {
        return this.commandMap.values();
    }

    public @NotNull Optional<IndexedCommand> findOptional(String name) {
        return this.commandMap.values().stream().filter(
            indexedCommand -> indexedCommand.getName().equalsIgnoreCase(name) || List.of(
                indexedCommand.getAliases()).contains(name)).findFirst();
    }

    public void registerCommands(Class<?>... commandClasses) {
        for (Class<?> commandClass : commandClasses) {
            registerCommand(commandClass);
        }
    }

    /**
     * Processing the given input and handling command execution by name
     *
     * @param commandSender which send the command
     * @param input         which the commandSender typed
     */
    public void execute(
        CommandSender commandSender,
        @NotNull List<String> input
    ) {
        // skip method execution if there is no input
        if (input.isEmpty()) {
            return;
        }

        // loop through all commands
        for (IndexedCommand command : this.commandMap.values()) {
            // get first argument
            final String name = input.get(0);
            // check if the command default name or the aliases are equals to the typed name. If not continue to the next element
            if (!command.getName().equalsIgnoreCase(name) && !List.of(command.getAliases())
                .contains(name)) {
                continue;
            }

            // remove first argument (name)
            input.remove(0);

            // execute command so the sub commands could be used
            command.execute(commandSender, input);
            return;
        }
        System.out.println("No command found!");
    }

    public void setOnCommandRegister(Consumer<IndexedCommand> onCommandRegister) {
        if (this.onCommandRegister != null) {
            return;
        }

        this.onCommandRegister = onCommandRegister;
    }
}
