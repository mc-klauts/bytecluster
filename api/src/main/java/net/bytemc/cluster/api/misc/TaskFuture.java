package net.bytemc.cluster.api.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class TaskFuture<T> {

    private T value;
    private final Map<State, List<Object>> listeners = new HashMap<>();

    public void complete(T value) {
        this.value = value;
        this.listeners.get(State.SUCCESS).forEach(it -> ((Consumer<T>) it).accept(value));
    }

    public void cancel(String reason) {
        this.listeners.get(State.FAILURE).forEach(it -> ((Consumer<String>) it).accept(reason));
    }

    public T get() {
        return this.value;
    }

    public enum State {
        SUCCESS,
        FAILURE
    }

    public TaskFuture<T> onComplete(Consumer<T> value) {
        listeners.put(State.SUCCESS, ListHelper.getOrCreateAndElement(listeners.getOrDefault(State.SUCCESS, new ArrayList<>()), value));
        return this;
    }

    public TaskFuture<T> onCancel(Consumer<String> value) {
        listeners.put(State.FAILURE, ListHelper.getOrCreateAndElement(listeners.getOrDefault(State.FAILURE, new ArrayList<>()), value));
        return this;
    }
}
