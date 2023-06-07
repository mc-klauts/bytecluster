package net.bytemc.cluster.api.misc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class UnsafeAccess {

    private static final Object THE_UNSAFE;

    static {
        Object unsafeInstance = null;
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafeInstance = theUnsafe.get(null);
        } catch (Exception ignored) {
        }
        THE_UNSAFE = unsafeInstance;
    }

    public static <T> T allocate(Class<T> clazz) {
        try {
            return (T) THE_UNSAFE.getClass().getDeclaredMethod("allocate", Class.class).invoke(THE_UNSAFE, clazz);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
