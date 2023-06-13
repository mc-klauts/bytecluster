package net.bytemc.cluster.node.misc;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

public final class VelocityForwardingSecretHelper {

    public static String TOKEN = generateToken();

    public static void generate(@NotNull Path path) {
        // create forwarding secret if not exists to prevent runtime exception
        final var forwardingSecret = path.resolve("forwarding.secret");

        if (Files.exists(forwardingSecret)) {
            try {
                Files.delete(forwardingSecret);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (final var writer = Files.newBufferedWriter(forwardingSecret)) {
            writer.write(TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateToken() {
        //generate secureToken
        final var chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
        final var builder = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            builder.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
        }
        return builder.toString();
    }
}
