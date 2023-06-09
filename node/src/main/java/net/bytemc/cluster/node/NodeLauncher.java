package net.bytemc.cluster.node;

import net.bytemc.cluster.node.exceptions.DefaultUncaughtExceptionHandler;

public final class NodeLauncher {

    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler());
        new Node();
    }

}
