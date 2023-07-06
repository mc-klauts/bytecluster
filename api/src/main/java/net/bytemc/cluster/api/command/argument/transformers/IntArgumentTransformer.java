package net.bytemc.cluster.api.command.argument.transformers;

import java.lang.reflect.Parameter;
import java.util.regex.Pattern;
import net.bytemc.cluster.api.command.argument.ArgumentTransformer;

public final class IntArgumentTransformer implements
    ArgumentTransformer<Integer> {

    private final Pattern pattern = Pattern.compile("-?[0-9]+");

    @Override
    public Integer transform(
        Parameter parameter,
        String input
    ) {
        return this.pattern.matcher(input).matches() ? Integer.parseInt(input) : -1;
    }
}
