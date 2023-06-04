package net.bytemc.cluster.launcher.misc;

import java.util.concurrent.TimeUnit;

public final class ThreadHelper {

    public static ThreadHelper HANDLER = new ThreadHelper();

    public ThreadHelper await(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void exit() {
        System.exit(1);
    }

}
