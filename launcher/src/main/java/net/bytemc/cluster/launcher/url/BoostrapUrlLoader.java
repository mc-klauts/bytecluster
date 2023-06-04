package net.bytemc.cluster.launcher.url;

import java.net.URL;
import java.net.URLClassLoader;

public final class BoostrapUrlLoader extends URLClassLoader {

    public BoostrapUrlLoader(URL[] urls, ClassLoader parent) {
        super("ByteMC-Cluster-Loader", urls, parent);
    }

    @Override
    protected void addURL(URL url) {
        super.addURL(url);
    }
}
