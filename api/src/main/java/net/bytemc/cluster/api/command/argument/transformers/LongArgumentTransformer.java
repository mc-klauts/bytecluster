package net.bytemc.cluster.api.command.argument.transformers;

import java.lang.reflect.Parameter;
import java.util.regex.Pattern;
import net.bytemc.cluster.api.command.argument.ArgumentTransformer;

public final class LongArgumentTransformer implements
    ArgumentTransformer<Long> {

    private final Pattern pattern = Pattern.compile("-?[0-9]+");

    @Override
    public Long transform(
        Parameter parameter,
        String input) {
        return this.pattern.matcher(input).matches() ? Long.parseLong(input) : -1;
    }
}
