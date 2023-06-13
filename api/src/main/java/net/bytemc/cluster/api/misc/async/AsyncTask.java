package net.bytemc.cluster.api.misc.async;

import java.util.concurrent.*;

//todo @cloudnet
public final class AsyncTask<T> extends CompletableFuture<T> {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static <T> AsyncTask<T> run(Runnable runnable) {
        return run(() -> {
            runnable.run();
            return null;
        });
    }

    public AsyncTask() {
        super.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public static <T> AsyncTask<T> run(ExceptionBroker<T, Throwable> handle) {
        final var worker = new AsyncTask<T>();
        EXECUTOR_SERVICE.execute(() -> {
            try {
                worker.complete(handle.get());
            } catch (Throwable throwable) {
                worker.completeExceptionally(throwable);
            }
        });
        return worker;
    }

    public static <T> AsyncTask<T> future(CompletableFuture<T> future) {
        final var worker = new AsyncTask<T>();
        future.whenComplete((result, exception) -> {
            if (exception == null) worker.complete(result);
            else worker.completeExceptionally(exception);
        });
        return worker;
    }

    public static <T> AsyncTask<T> completeWork(Object result) {
        final var worker = new AsyncTask<T>();
        if (result instanceof Throwable throwable) {
            worker.completeExceptionally(throwable);
        } else {
            worker.complete((T) result);
        }
        return worker;
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
}
