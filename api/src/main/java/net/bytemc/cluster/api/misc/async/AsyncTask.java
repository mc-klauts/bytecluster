package net.bytemc.cluster.api.misc.async;

import java.util.concurrent.*;

public final class AsyncTask<R> extends CompletableFuture<R> {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public AsyncTask() {
        // if completeExceptionally is called, the exception will be printed
        super.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public R getSync(R object) {
        return this.get(5, TimeUnit.SECONDS, object);
    }

    public R get(long time, TimeUnit timeUnit, R exceptionObject) {
        try {
            return this.get(time, timeUnit);
        } catch (CancellationException | ExecutionException | InterruptedException | TimeoutException e) {
            return exceptionObject;
        }
    }

    public static <R> AsyncTask<R> async(ExceptionBroker<R, Throwable> handle) {
        final var worker = new AsyncTask<R>();
        EXECUTOR.execute(() -> {
            try {
                worker.complete(handle.get());
            } catch (Throwable throwable) {
                worker.completeExceptionally(throwable);
            }
        });
        return worker;
    }

    public static <R> AsyncTask<R> directly(Object result) {
        final var worker = new AsyncTask<R>();
        if (result instanceof Throwable throwable) {
            worker.completeExceptionally(throwable);
        } else {
            worker.complete((R) result);
        }
        return worker;
    }
}
