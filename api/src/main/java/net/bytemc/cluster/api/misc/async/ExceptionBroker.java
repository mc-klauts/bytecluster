package net.bytemc.cluster.api.misc.async;

@FunctionalInterface
public interface ExceptionBroker<O, T extends Throwable> {

    O get() throws T;

}