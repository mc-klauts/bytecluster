package net.bytemc.cluster.node.exceptions;

import net.bytemc.cluster.node.logger.Logger;

public final class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger.error(e.getMessage(), e);
    }
}
