package net.bytemc.cluster.node.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Path;

public final class ConfigurationProvider {

    private static final Gson DEFAULT_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public static void write(Path path, Object value) {
        try(FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(DEFAULT_GSON.toJson(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T read(Path path, Class<T> value) {
        try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            return DEFAULT_GSON.fromJson(reader, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
