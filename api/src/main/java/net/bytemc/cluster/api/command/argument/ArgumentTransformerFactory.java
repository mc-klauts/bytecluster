package net.bytemc.cluster.api.command.argument;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ArgumentTransformerFactory {

    private static final Map<Class<? extends ArgumentTransformer<?>>, ArgumentTransformer<?>> transformerCache = new HashMap<>();

    @Contract(pure = true)
    public static ArgumentTransformer<?> create(@NotNull ArgumentTransformerType argumentTransformerType) {
        return argumentTransformerType.getArgumentTransformer();
    }

    public static ArgumentTransformer<?> getOrCreate(Class<? extends ArgumentTransformer<?>> transformerClass) {
        return transformerCache.computeIfAbsent(transformerClass, aClass -> {
            final Optional<ArgumentTransformer<?>> optionalArgumentTransformer = find(
                transformerClass);
            if (optionalArgumentTransformer.isPresent()) {
                return optionalArgumentTransformer.get();
            }

            try {
                return transformerClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private static Optional<ArgumentTransformer<?>> find(Class<? extends ArgumentTransformer<?>> transformerClass) {
        for (ArgumentTransformerType value : ArgumentTransformerType.values()) {
            if (value.getArgumentTransformer().getClass() == transformerClass) {
                return Optional.of(value.getArgumentTransformer());
            }
        }
        return Optional.empty();
    }

}
