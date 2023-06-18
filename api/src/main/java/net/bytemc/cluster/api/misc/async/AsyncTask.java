package net.bytemc.cluster.api.misc.async;

import java.util.concurrent.*;

public final class AsyncTask<T> extends CompletableFuture<T> {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public AsyncTask() {
        // if completeExceptionally is called, the exception will be printed
        super.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public T getSync(T def) {
        return this.get(5, TimeUnit.SECONDS, def);
    }

    public T get(long time, TimeUnit timeUnit, T def) {
        try {
            return this.get(time, timeUnit);
        } catch (CancellationException | ExecutionException | InterruptedException | TimeoutException e) {
            return def;
        }
    }

    public static <T> AsyncTask<T> async(ExceptionBroker<T, Throwable> handle) {
        final var worker = new AsyncTask<T>();
        EXECUTOR.execute(() -> {
            try {
                worker.complete(handle.get());
            } catch (Throwable throwable) {
                worker.completeExceptionally(throwable);
            }
        });
        return worker;
    }

    public static <T> AsyncTask<T> directly(Object result) {
        final var worker = new AsyncTask<T>();
        if (result instanceof Throwable throwable) {
            worker.completeExceptionally(throwable);
        } else {
            worker.complete((T) result);
        }
        return worker;
    }
}
