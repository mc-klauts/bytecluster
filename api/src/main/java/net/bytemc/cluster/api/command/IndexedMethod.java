package net.bytemc.cluster.api.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import net.bytemc.cluster.api.command.annotations.CommandParameter;
import net.bytemc.cluster.api.command.argument.ArgumentTransformer;
import net.bytemc.cluster.api.command.argument.ArgumentTransformerFactory;
import net.bytemc.cluster.api.command.argument.transformers.PlainTextTransformer;
import net.bytemc.cluster.api.command.autocompletion.TabCompleter;
import net.bytemc.cluster.api.command.commandsender.CommandSender;

@RequiredArgsConstructor
@Getter
public final class IndexedMethod {

    //region parameter
    private final Map<Integer, CommandParameter> commandParameterHashMap = new HashMap<>();
    private final Object ownerClass;
    //endregion parameter
    private final IndexedCommand ownerCommand;
    private final Method method;
    private final String name;
    private final String description;
    private final String permission;
    private final TabCompleter tabCompleter;
    private boolean loaded;

    public void invoke(
        @NotNull CommandSender commandSender,
        List<String> args
    ) {

        if (!commandSender.hasPermission(permission)) {
            commandSender.sendNonePermission();
            return;
        }

        if (!this.loaded) {
            this.loadParameters();
            this.loaded = true;
        }

        boolean plainText = this.commandParameterHashMap.values().stream().anyMatch(
            commandParameter -> commandParameter.transformer()
                == PlainTextTransformer.class);
        final Object[] objects = new Object[plainText ? 2 : args.toArray().length + 1];

        if (plainText) {
            objects[1] = "";

            if (!args.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String arg : args) {
                    stringBuilder.append(arg).append(" ");
                }

                objects[1] = ArgumentTransformerFactory.getOrCreate(
                        PlainTextTransformer.class)
                    .transform(this.method.getParameters()[1],
                        stringBuilder.substring(0, stringBuilder.length() - 1));
            }
        } else {
            if (objects.length != this.method.getParameters().length) {
                this.sendInvalidCommand(commandSender);
                return;
            }
        }

        objects[0] = commandSender;

        if (!plainText) {
            for (int i = 0; i < args.size(); i++) {
                final String writtenArgument = args.get(i);
                final CommandParameter commandArgument = this.commandParameterHashMap.get(i);

                if (commandArgument == null) {
                    continue;
                }

                final ArgumentTransformer<?> argumentTransformer = ArgumentTransformerFactory.getOrCreate(
                    commandArgument.transformer());

                objects[i + 1] = argumentTransformer
                    .transform(this.method.getParameters()[i + 1], writtenArgument);
            }
        }

        try {
            this.method.setAccessible(true);
            this.method.invoke(ownerClass, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void sendInvalidCommand(CommandSender commandSender) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (CommandParameter value : this.commandParameterHashMap.values()) {
            stringBuilder.append(value.name()).append(" ");
        }
        commandSender.sendMessage(
            this.ownerCommand.getName() + " " + this.name + " " + stringBuilder);
    }

    /**
     * Loop through all method parameters and load all for easy argument transformation
     */
    private void loadParameters() {
        for (int i = 1; i < this.method.getParameters().length; i++) {
            final Parameter parameter = this.method.getParameters()[i];
            if (parameter.isAnnotationPresent(CommandParameter.class)) {
                final CommandParameter parameterAnnotation = parameter.getAnnotation(
                    CommandParameter.class);
                this.commandParameterHashMap.put(i - 1, parameterAnnotation);
            }
        }
    }
}
