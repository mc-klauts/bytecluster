package net.bytemc.cluster.api.command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.DefaultExecution;
import net.bytemc.cluster.api.command.annotations.SubCommand;
import net.bytemc.cluster.api.command.autocompletion.DefaultTabCompleter;

@RequiredArgsConstructor
@Getter
public final class CommandCreator {

    private final Class<?> commandClass;
    private final Map<String, IndexedMethod> indexedMethodMap = new HashMap<>();
    private Object commandInstance;
    private IndexedCommand indexedCommand;

    /**
     * Managing command / subcommand and information loading. Also creating the instance to invoke
     * custom methods.
     */
    @SuppressWarnings("deprecation")
    @SneakyThrows
    public void handle() {
        // create unsafe command instance
        this.commandInstance = this.commandClass.newInstance();

        // get command annotation for global command information
        final Command command = this.commandClass.getAnnotation(Command.class);

        // put collected data into separate indexed command section
        this.indexedCommand = new IndexedCommand(command.name(), command.aliases(),
            command.permission(), null, command.tabCompleter().newInstance());

        this.indexedCommand.setDefaultExecution(this.indexDefaultExecution(
            Arrays.stream(this.commandClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(DefaultExecution.class))
                .findAny()));

        // load subcommands
        final List<Method> subCommandMethods = Arrays.stream(this.commandClass.getDeclaredMethods())
            .filter(method -> {
                method.setAccessible(true);
                return method.isAnnotationPresent(SubCommand.class);
            }).toList();
        subCommandMethods.forEach(this::indexSubCommand);
        // put all indexed commands into the command subcommand map
        this.indexedCommand.getSubCommandMap().putAll(this.indexedMethodMap);
    }

    /**
     * Load command information from the {@link SubCommand} annotation annotated on the given
     * method
     *
     * @param method we want to index
     */
    @SuppressWarnings("deprecation")
    @SneakyThrows
    private void indexSubCommand(@NotNull Method method) {
        final SubCommand annotation = method.getAnnotation(SubCommand.class);
        final String name = annotation.name();
        final String description = annotation.description();
        final String permission = annotation.permission();
        this.indexedMethodMap.put(name,
            new IndexedMethod(this.commandInstance, this.indexedCommand, method, name, description,
                permission, annotation.tabCompleter().newInstance()));
    }

    /**
     * Select an indexed method based on an optional method which could be found in the class
     *
     * @param optionalMethod we don't know if this method exists or not
     * @return an indexed method or null if nothing is set
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private @Nullable IndexedMethod indexDefaultExecution(@NotNull Optional<Method> optionalMethod) {
        final AtomicReference<IndexedMethod> atomicReference = new AtomicReference<>(null);
        optionalMethod.ifPresent(
            method -> atomicReference.set(
                new IndexedMethod(this.commandInstance, this.indexedCommand, method, "", "",
                    "", new DefaultTabCompleter())));
        return atomicReference.get();
    }
}
