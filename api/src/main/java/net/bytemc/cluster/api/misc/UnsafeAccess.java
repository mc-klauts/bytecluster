package net.bytemc.cluster.api.misc;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

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
            return (T) THE_UNSAFE.getClass().getDeclaredMethod("allocateInstance", Class.class).invoke(THE_UNSAFE, clazz);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isInJar(Class<?> clazz, File file) {
        var location = clazz.getResource('/' + clazz.getName().replace('.', '/') + ".class");
        var args = location.getFile().split("!")[0].split("/");

        //IMPROVE find a better way

        return file.getName().equals(args[args.length-1]);
    }


}
