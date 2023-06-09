package net.bytemc.cluster.api.command.argument.transformers;

import java.util.regex.Pattern;
import net.bytemc.cluster.api.command.argument.ArgumentTransformer;

public final class FloatArgumentTransformer implements
    ArgumentTransformer<Float> {

    private final Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");

    @Override
    public Float transform(String input) {
        return this.pattern.matcher(input).matches() ? Float.parseFloat(input) : -1f;
    }
}
