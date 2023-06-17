package net.bytemc.cluster.node.modules;

import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.api.misc.FileHelper;
import net.bytemc.cluster.node.configuration.ConfigurationProvider;
import net.bytemc.cluster.node.exception.ModuleLoadException;
import net.bytemc.cluster.node.modules.content.LoadedModuleFileContent;
import net.bytemc.cluster.node.modules.content.ModuleContentInfo;
import net.bytemc.cluster.node.modules.loader.ModuleClassLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public final class CloudModuleHandler {

    private static Path MODULE_PATH = Path.of("modules");

    private List<LoadedModule> loadedModules = new CopyOnWriteArrayList<>();

    public CloudModuleHandler() {
        FileHelper.createDirectoryIfNotExists(MODULE_PATH);
        this.loadModules();
    }

    public void loadModules() {
        for (LoadedModuleFileContent content : getAllCloudModuleFileInfos()) {
            try {
                var classLoader = new ModuleClassLoader(new URL[]{content.getFile().toURI().toURL()}, this.getClass().getClassLoader(), content.getInfo().getName(), this);
                var cloudModule = loadModuleClassInstance(classLoader, content.getInfo().getMainClass());
                var loadedModule = new LoadedModule(cloudModule, content.getFile(), content.getInfo(), classLoader);

                Logger.info("Loaded module " + loadedModule.getInfo().getName() + " by " + loadedModule.getInfo().getName());
                this.loadedModules.add(loadedModule);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Logger.info("Loaded successfully &b" + loadedModules.size() + "&7 modules&8.");

        for (LoadedModule module : this.loadedModules) {
            module.getModule().onEnable();
        }
    }

    private CloudModule loadModuleClassInstance(ClassLoader classLoader, String mainClassName) throws Exception {
        return loadModuleClass(classLoader, mainClassName).getConstructor().newInstance();
    }

    private Class<? extends CloudModule> loadModuleClass(ClassLoader classLoader, String mainClassName) throws ClassNotFoundException {
        return  classLoader.loadClass(mainClassName).asSubclass(CloudModule.class);
    }

    public ModuleContentInfo getInfoFromFile(File file, String path) {
        try {
            if (!file.exists()) {
                throw new IllegalArgumentException("Specified file to load " + path + " from does not exist: " + file.getPath());
            }
            var jar = new JarFile(file);
            var entry = jar.getJarEntry(path);
            if (entry == null) {
                throw new ModuleLoadException(file.getPath() + ": No '" + path + "' found.");
            }
            var fileStream = jar.getInputStream(entry);
            var gsonText = FileHelper.loadFromInputStream(fileStream);
            jar.close();
            var object = ConfigurationProvider.DEFAULT_GSON.fromJson(gsonText, ModuleContentInfo.class);
            if (object == null) {
                throw new ModuleLoadException(file.getPath() + ": Invalid '" + path + "'.");
            }
            return object;
        } catch (IOException ex) {
            throw new ModuleLoadException(file.getPath());
        }
    }

    private List<File> getAllModuleJarFiles() {
        return List.of(MODULE_PATH.toFile().listFiles());
    }

    private List<LoadedModuleFileContent> getAllCloudModuleFileInfos() {
        return getAllModuleJarFiles().stream().map(it -> new LoadedModuleFileContent(it, getInfoFromFile(it, "module.json"))).toList();
    }

    public Class<?> findModuleClass(String name) throws ClassNotFoundException {
        List<Class<?>> classes = loadedModules.stream()
                .map(module -> {
                    try {
                        return module.getClassLoader().findClass0(name, false);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!classes.isEmpty()) {
            return classes.get(0);
        } else {
            throw new ClassNotFoundException(name);
        }
    }
}
