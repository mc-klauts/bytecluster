package net.bytemc.cluster.api.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.bytemc.cluster.api.misc.gson.PathSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public final class GsonHelper {

    public static final Gson DEFAULT_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeHierarchyAdapter(Path.class, new PathSerializer())
            .disableHtmlEscaping()
            .create();

    public static final Gson SENDABLE_GSON = new GsonBuilder()
            .serializeNulls()
            .registerTypeHierarchyAdapter(Path.class, new PathSerializer())
            .disableHtmlEscaping()
            .create();

    public static void write(@NotNull Path path, Object value) {
        try(var writer = new FileWriter(path.toFile())) {
            writer.write(DEFAULT_GSON.toJson(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> @Nullable T read(Path path, Class<T> value) {
        try(var reader = new BufferedReader(new FileReader(path.toFile()))) {
            return DEFAULT_GSON.fromJson(reader, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
