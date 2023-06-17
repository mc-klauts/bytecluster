package net.bytemc.cluster.node.modules.loader;

import net.bytemc.cluster.node.modules.CloudModuleHandler;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ModuleClassLoader extends URLClassLoader {

    private volatile boolean closed = false;
    private final Map<String, Class<?>> cachedClasses = new ConcurrentHashMap<>();
    private CloudModuleHandler moduleHandler;

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public ModuleClassLoader(URL[] urls, ClassLoader parent, String moduleName, CloudModuleHandler moduleHandler) {
        super(moduleName, urls, parent);
        this.moduleHandler = moduleHandler;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (closed) {
            throw new IllegalStateException("ModuleClassLoader is already closed");
        }
        return findClass0(name, true);
    }

    public Class<?> findClass0(String name, boolean checkGlobal) throws ClassNotFoundException {
        Class<?> clazz = cachedClasses.get(name);
        if (clazz != null) {
            return clazz;
        }
        Class<?> classByName = null;
        try {
            classByName = super.findClass(name);
        } catch (ClassNotFoundException e) {
            // Ignore exception
        }
        if (classByName != null) {
            cachedClasses.put(name, classByName);
            return classByName;
        }
        if (checkGlobal) {
            Class<?> otherModuleClass = moduleHandler != null ? moduleHandler.findModuleClass(name) : null;
            if (otherModuleClass != null) {
                cachedClasses.put(name, otherModuleClass);
                return otherModuleClass;
            }
        }
        throw new ClassNotFoundException(name);
    }

    @Override
    public void close() throws IOException {
        super.close();
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

}
