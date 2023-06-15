package net.bytemc.cluster.api.command.argument;

import java.lang.reflect.Parameter;

public interface ArgumentTransformer<T> {

    T transform(
        Parameter parameter,
        String input);

}
