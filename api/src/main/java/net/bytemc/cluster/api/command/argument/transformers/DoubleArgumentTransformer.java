package net.bytemc.cluster.api.command.argument.transformers;

import java.util.regex.Pattern;
import net.bytemc.cluster.api.command.argument.ArgumentTransformer;

public final class DoubleArgumentTransformer implements
    ArgumentTransformer<Double> {

    private final Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");

    @Override
    public Double transform(String input) {
        return this.pattern.matcher(input).matches() ? Double.parseDouble(input) : -1D;
    }
}
