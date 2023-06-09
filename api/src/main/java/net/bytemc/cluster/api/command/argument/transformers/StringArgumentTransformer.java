package net.bytemc.cluster.api.command.argument.transformers;

import net.bytemc.cluster.api.command.argument.ArgumentTransformer;

public final class StringArgumentTransformer implements
    ArgumentTransformer<String> {

    @Override
    public String transform(String input) {
        return input;
    }
}
