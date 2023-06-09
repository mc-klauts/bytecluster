package net.bytemc.cluster.api.command.argument.transformers;

import java.util.List;
import net.bytemc.cluster.api.command.argument.ArgumentTransformer;
import org.jetbrains.annotations.NotNull;

public final class BooleanArgumentTransformer implements
    ArgumentTransformer<Boolean> {

    private static final List<String> validStrings = List.of("TRUE", "FALSE");

    @Override
    public @NotNull Boolean transform(@NotNull String input) {
        return validStrings.contains(input.toUpperCase()) && Boolean.parseBoolean(
            input.toUpperCase());
    }
}
