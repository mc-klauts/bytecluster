package net.bytemc.cluster.api.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.bytemc.cluster.api.command.annotations.Command;
import net.bytemc.cluster.api.command.annotations.CommandHelpTopic;
import net.bytemc.cluster.api.command.annotations.DefaultCommand;
import net.bytemc.cluster.api.command.annotations.SubCommand;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public final class InternalCommand {

    private final Object commandInstance;
    private final Map<String, IndexedCommandMethod> commandMethodMap = new HashMap<>();

    private final List<String> callNames = new ArrayList<>();
    private final List<String> helpTopic = new ArrayList<>();
    private Method defaultMethod;

    @SneakyThrows
    public boolean tryExecute(
        CommandSender commandSender,
        @NotNull String input
    ) {
        final String[] splitText = input.split(" ");
        if (splitText.length == 0) {
            this.sendHelpTopic(commandSender);
            return false;
        }

        final List<String> arguments = new ArrayList<>(List.of(splitText));
        final String callArg = arguments.get(0);
        arguments.remove(0);
        if (!this.callNames.contains(callArg)) {
            return false;
        }
        if (this.commandMethodMap.isEmpty()) {
            if (this.defaultMethod != null) {
                this.defaultMethod.invoke(this.commandInstance, commandSender);
            }
        }

        if (splitText.length >= 2) {
            final String methodCall = arguments.get(0);
            arguments.remove(0);
            final IndexedCommandMethod indexedCommandMethod = this.commandMethodMap.get(methodCall);
            if (indexedCommandMethod == null) {
                this.sendHelpTopic(commandSender);
                return false;
            }

            indexedCommandMethod.invoke(commandSender, arguments);
        }
        return true;
    }

    public void sendHelpTopic(CommandSender commandSender) {
        if (this.commandInstance.getClass().isAnnotationPresent(CommandHelpTopic.class)) {
            final CommandHelpTopic commandHelpTopicAnnotation = this.commandInstance.getClass()
                .getAnnotation(CommandHelpTopic.class);
            for (String message : commandHelpTopicAnnotation.messages()) {
                commandSender.sendMessage(message);
            }
        }
    }

    public void initialize() {
        if (commandInstance == null) {
            return;
        }

        final Class<?> instancedClass = this.commandInstance.getClass();
        if (!instancedClass.isAnnotationPresent(Command.class)) {
            return;
        }

        final Command commandAnnotation = instancedClass.getAnnotation(Command.class);
        this.callNames.add(commandAnnotation.name());
        this.callNames.addAll(List.of(commandAnnotation.aliases()));

        for (Method declaredMethod : instancedClass.getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            if (declaredMethod.isAnnotationPresent(SubCommand.class)) {
                final SubCommand subCommandAnnotation = declaredMethod.getAnnotation(
                    SubCommand.class);
                final IndexedCommandMethod indexedCommandMethod = new IndexedCommandMethod(
                    this.commandInstance,
                    declaredMethod,
                    subCommandAnnotation.name(), subCommandAnnotation.example());
                indexedCommandMethod.loadArguments();
                this.commandMethodMap.put(subCommandAnnotation.name(),
                    indexedCommandMethod);
            } else if (declaredMethod.isAnnotationPresent(DefaultCommand.class)) {
                this.defaultMethod = declaredMethod;
            }
        }
    }
}
