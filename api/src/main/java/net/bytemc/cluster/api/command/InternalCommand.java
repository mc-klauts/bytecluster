package net.bytemc.cluster.api.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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
import net.bytemc.cluster.api.command.autocompletion.TabCompleter;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import net.bytemc.cluster.api.logging.Logger;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public final class InternalCommand {

    private final Object commandInstance;
    private final Map<String, IndexedCommandMethod> commandMethodMap = new HashMap<>();

    private final List<String> callNames = new ArrayList<>();
    private final List<String> helpTopic = new ArrayList<>();
    private Method defaultMethod;
    private TabCompleter tabCompleter;

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
        final String callArg = arguments.remove(0);
        if (!this.callNames.contains(callArg)) {
            return false;
        }
        if (this.commandMethodMap.isEmpty() || splitText.length < 2) {
            if (this.defaultMethod != null) {
                this.defaultMethod.invoke(this.commandInstance, commandSender);
            }
        }

        if (splitText.length >= 2) {
            final String methodCall = arguments.remove(0);
            final IndexedCommandMethod indexedCommandMethod = this.commandMethodMap.get(methodCall);
            if (indexedCommandMethod == null) {
                this.sendHelpTopic(commandSender);
                return false;
            }

            indexedCommandMethod.invoke(commandSender, arguments);
        }
        return true;
    }

    private void sendHelpTopic(CommandSender commandSender) {
        if (this.commandInstance.getClass().isAnnotationPresent(CommandHelpTopic.class)) {
            final CommandHelpTopic commandHelpTopicAnnotation = this.commandInstance.getClass()
                .getAnnotation(CommandHelpTopic.class);

            for (String message : commandHelpTopicAnnotation.messages()) {
                commandSender.sendMessage(message);
            }
        } else {
            commandSender.sendMessage("No help for this command.");
        }
    }

    @SneakyThrows
    public void index() {
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
        this.tabCompleter = commandAnnotation.tabCompleter().newInstance();

        for (Method declaredMethod : instancedClass.getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            if (declaredMethod.isAnnotationPresent(SubCommand.class)) {
                final SubCommand subCommandAnnotation = declaredMethod.getAnnotation(
                    SubCommand.class);
                final IndexedCommandMethod indexedCommandMethod = new IndexedCommandMethod(
                    this.commandInstance,
                    declaredMethod,
                    subCommandAnnotation.name(), subCommandAnnotation.example(),
                    subCommandAnnotation.tabCompleter().newInstance());
                indexedCommandMethod.loadArguments();
                this.commandMethodMap.put(subCommandAnnotation.name(),
                    indexedCommandMethod);
            } else if (declaredMethod.isAnnotationPresent(DefaultCommand.class)) {
                this.defaultMethod = declaredMethod;
            }
        }
    }

    public List<String> tryTabComplete(
        CommandSender commandSender,
        @NotNull String input
    ) {
        //   1     2
        // group start
        final String[] arguments = input.split(" ");
        if (this.commandMethodMap.isEmpty() || arguments.length <= 1) {
            return this.tabCompleter.complete(commandSender, input, arguments);
        }

        final String callMethod = arguments[1];
        if (this.commandMethodMap.get(callMethod) != null) {
            return this.commandMethodMap.get(callMethod).getTabCompleter()
                .complete(commandSender, input, arguments);
        }
        return Collections.emptyList();
    }
}
