package net.bytemc.bytecluster.wrapper.loader;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public final class ApplicationExternalClassLoader extends URLClassLoader {

    @Getter
    private boolean closed = false;

    public ApplicationExternalClassLoader() {
        super(new URL[]{}, ClassLoader.getSystemClassLoader());
    }

    @SneakyThrows
    public ApplicationExternalClassLoader addUrl(@NotNull final Path url){
        this.addURL(url.toUri().toURL());
        return this;
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        super.close();
    }

}
