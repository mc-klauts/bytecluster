package net.bytemc.cluster.api.command.argument.transformers;

import java.lang.reflect.Parameter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import net.bytemc.cluster.api.command.argument.ArgumentTransformer;

public final class PlainTextTransformer implements ArgumentTransformer<String> {

    @Contract(pure = true)
    @Override
    public String transform(
        Parameter parameter,
        @NotNull String input
    ) {
        return input;
    }
}
