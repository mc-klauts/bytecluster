package net.bytemc.cluster.api.command.argument.transformers;

import java.lang.reflect.Parameter;
import net.bytemc.cluster.api.command.argument.ArgumentTransformer;

public final class StringArgumentTransformer implements
    ArgumentTransformer<String> {

    @Override
    public String transform(
        Parameter parameter,
        String input) {
        return input;
    }
}
