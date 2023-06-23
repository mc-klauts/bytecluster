package net.bytemc.cluster.api.misc.gson;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PathSerializer extends TypeAdapter<Path> {

    @Override
    public void write(@NonNull JsonWriter out, @Nullable Path value) throws IOException {
        out.value(value.toString().replace(File.separatorChar, '/'));
    }

    @Override
    public @Nullable Path read(@NonNull JsonReader in) throws IOException {
        var path = in.nextString();
        return path == null ? null : Path.of(path);
    }
}