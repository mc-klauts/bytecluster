package net.bytemc.bytecluster.wrapper.loader;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public final class ApplicationExternalClassLoader extends URLClassLoader {

    @Getter
    private boolean closed = false;

    public ApplicationExternalClassLoader() {
        super(new URL[]{}, ClassLoader.getSystemClassLoader());
    }

    public ApplicationExternalClassLoader addUrl(@NotNull final Path url){
        try {
            this.addURL(url.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        super.close();
    }

}
