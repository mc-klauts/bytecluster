package net.bytemc.bytecluster.wrapper.logging;

import net.bytemc.cluster.api.logging.Logger;

public final class WrapperLogging implements Logger {

    @Override
    public void logInfo(String text) {
        System.out.println(text);
    }

    @Override
    public void logWarn(String text) {
        System.err.println(text);
    }

    @Override
    public void logError(String text, Throwable exception) {
        System.err.println(text);
        exception.printStackTrace();
    }

    @Override
    public void logEmpty(String line) {
        System.out.println(line);
    }
}
