package net.bytemc.bytecluster.wrapper;

import net.bytemc.bytecluster.wrapper.loader.ApplicationExternalClassLoader;

import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class WrapperLauncher {

    private static Instrumentation instrumentation;
    private static boolean cacheInitialized = false;

    public static void premain(final String s, final Instrumentation instrumentation) {
        WrapperLauncher.instrumentation = instrumentation;
    }

    public static void main(String[] args) {
        try {
            final var wrapper = new Wrapper();
            //todo
          //  wrapper.getPacketHandler().registerPacketListener(CacheInitPacket.class, (channelHandlerContext, packet) -> cacheInitialized.set(true));

            final var arguments = new ArrayList<>(Arrays.asList(args));
            final var main = arguments.remove(0);
            final var applicationFile = Paths.get(arguments.remove(0));

            var classLoader = ClassLoader.getSystemClassLoader();
            if (Boolean.parseBoolean(arguments.remove(0))) {

                classLoader = new ApplicationExternalClassLoader().addUrl(Paths.get(arguments.remove(0)));
                try (final var jarInputStream = new JarInputStream(Files.newInputStream(applicationFile))) {
                    JarEntry jarEntry;
                    while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                        if (jarEntry.getName().endsWith(".class")) {
                            Class.forName(jarEntry.getName().replace('/', '.').replace(".class", ""), false, classLoader);
                        }
                    }
                }
            }

            instrumentation.appendToSystemClassLoaderSearch(new JarFile(applicationFile.toFile()));
            final var mainClass = Class.forName(main, true, classLoader);
            final var thread = new Thread(() -> {
                try {
                    mainClass.getMethod("main", String[].class).invoke(null, (Object) arguments.toArray(new String[0]));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }, "ByteCluster-Service-Thread");
            thread.setContextClassLoader(classLoader);
            if (cacheInitialized) {
                thread.start();
            } else {
              //  wrapper.getPacketHandler().registerPacketListener(CacheInitPacket.class, (channelHandlerContext, packet) -> thread.start());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
