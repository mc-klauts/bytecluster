package net.bytemc.cluster.node.console.commands;

import lombok.Getter;
import net.bytemc.cluster.node.Node;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@Getter
public abstract class CloudCommand {

    private final String name, description;
    private final String[] aliases;

    public CloudCommand() {
        final var annotation = getClass().getAnnotation(Command.class);
        this.name = annotation.name();
        this.description = annotation.description();
        this.aliases = annotation.aliases();
    }

    public abstract void execute(Node node, String[] args);

    public List<String> tabComplete(final String[] arguments) {
        return null;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Command {

        String name();
        String description() default "";
        String[] aliases() default {};

    }


}
