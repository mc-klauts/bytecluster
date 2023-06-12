package net.bytemc.cluster.api.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.command.annotations.CommandArgument;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public final class IndexedCommandMethod {

    private final Object ownerClass;
    private final Method method;
    private final String callName;
    private final String example;
    private final Map<Integer, CommandArgument> commandArguments = new HashMap<>();

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

    public void invoke(
        CommandSender commandSender,
        @NotNull List<String> arguments
    ) {
        Object[] objects = new Object[arguments.toArray().length + 1];
        objects[0] = commandSender;

        for (int i = 0; i < arguments.size(); i++) {
            final String writtenArgument = arguments.get(i);
            final CommandArgument commandArgument = this.commandArguments.get(i);
            if (commandArgument == null) {
                return;
            }
            objects[i + 1] = commandArgument.transformer().getArgumentTransformer()
                .transform(writtenArgument);
        }

        if (objects.length < this.method.getParameters().length - 1) {
            commandSender.sendMessage(this.example);
            return;
        }

        try {
            this.method.invoke(ownerClass, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
