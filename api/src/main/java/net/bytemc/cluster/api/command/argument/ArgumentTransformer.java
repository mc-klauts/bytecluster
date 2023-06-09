package net.bytemc.cluster.api.command.argument;

public interface ArgumentTransformer<T> {

    T transform(String input);

}
