package net.bytemc.cluster.api.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.bytemc.cluster.api.command.argument.ArgumentTransformerType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface CommandArgument {

    String name();

    ArgumentTransformerType transformer();


}
