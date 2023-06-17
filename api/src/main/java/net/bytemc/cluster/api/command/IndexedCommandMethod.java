package net.bytemc.cluster.api.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.command.annotations.CommandArgument;
import net.bytemc.cluster.api.command.argument.ArgumentTransformerFactory;
import net.bytemc.cluster.api.command.autocompletion.TabCompleter;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public final class IndexedCommandMethod {

    private final Map<Integer, CommandArgument> commandArguments = new HashMap<>();
    private final Object ownerClass;
    private final Method method;
    private final String callName;
    private final String example;
    private final TabCompleter tabCompleter;

    public void invoke(
        CommandSender commandSender,
        @NotNull List<String> arguments
    ) {

        final Object[] objects = new Object[arguments.toArray().length + 1];

        if (objects.length <= this.method.getParameters().length - 1) {
            commandSender.sendMessage(this.example);
            return;
        }

        objects[0] = commandSender;

        for (int i = 0; i < arguments.size(); i++) {
            final String writtenArgument = arguments.get(i);
            final CommandArgument commandArgument = this.commandArguments.get(i);
            if (commandArgument == null) {
                return;
            }
            objects[i + 1] = ArgumentTransformerFactory.getOrCreate(commandArgument.transformer())
                .transform(this.method.getParameters()[i + 1], writtenArgument);
        }

        try {
            this.method.invoke(ownerClass, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void loadArguments() {
        for (int i = 1; i < this.method.getParameters().length; i++) {
            final Parameter parameter = this.method.getParameters()[i];
            if (parameter.isAnnotationPresent(CommandArgument.class)) {
                final CommandArgument commandArgumentAnnotation = parameter.getAnnotation(
                    CommandArgument.class);
                this.commandArguments.put(i - 1, commandArgumentAnnotation);
            }
        }
    }
}
