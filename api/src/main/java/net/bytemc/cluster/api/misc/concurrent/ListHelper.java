package net.bytemc.cluster.api.misc.concurrent;

import java.util.List;

public final class ListHelper {

    public static <T> List<T> addElementInList(List<T> list, T element) {
        list.add(element);
        return list;
    }
}
