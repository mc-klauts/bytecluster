package net.bytemc.cluster.api.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.bytemc.cluster.api.command.autocompletion.DefaultTabCompleter;
import net.bytemc.cluster.api.command.autocompletion.TabCompleter;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubCommand {

    String name();

    String description() default "";

    String permission() default "";
    Class<? extends TabCompleter> tabCompleter() default DefaultTabCompleter.class;

}
