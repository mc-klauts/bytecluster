package net.bytemc.cluster.api.misc;

import java.util.List;

public final class ListHelper {

    public static <T> List<T> getOrCreateAndElement(List<T> list, T value) {
        list.add(value);
        return list;
    }
}
