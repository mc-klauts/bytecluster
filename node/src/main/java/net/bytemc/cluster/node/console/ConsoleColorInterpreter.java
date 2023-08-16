package net.bytemc.cluster.node.console;

import lombok.NonNull;
import java.util.regex.Pattern;

public final class ConsoleColorInterpreter {

    private static final Color[] VALUES = Color.values();
    private static final String LOOKUP = "0123456789abcdefklmnor";

    public static @NonNull String toColoredString(char triggerChar, @NonNull String input) {
        var contentBuilder = new StringBuilder(convertRGBColors(triggerChar, input));
        var breakIndex = contentBuilder.length() - 1;
        for (var i = 0; i < breakIndex; i++) {
            if (contentBuilder.charAt(i) != triggerChar) {
                continue;
            }
            var format = LOOKUP.indexOf(contentBuilder.charAt(i + 1));
            if (format == -1) {
                continue;
            }
            var ansiCode = VALUES[format].getAnsi();
            contentBuilder.delete(i, i + 2).insert(i, ansiCode);
            breakIndex += ansiCode.length() - 2;
        }
        return contentBuilder.toString();
    }

    private static @NonNull String convertRGBColors(char triggerChar, @NonNull String input) {
        var pattern = Pattern.compile(triggerChar + "#([\\da-fA-F]){6}");
        return pattern.matcher(input).replaceAll(result -> {
            int hexInput = Integer.decode(result.group().substring(1));
            return String.format("\u001B[38;2;%d;%d;%dm", (hexInput >> 16) & 0xFF, (hexInput >> 8) & 0xFF, hexInput & 0xFF);
        });
    }

    public static @NonNull String stripColor(char triggerChar, @NonNull String input) {
        var replacePattern = Pattern.compile(triggerChar + "#([\\da-fA-F]){6}");
        var contentBuilder = new StringBuilder(replacePattern.matcher(input).replaceAll(""));
        var breakIndex = contentBuilder.length() - 1;
        for (var i = 0; i < breakIndex; i++) {
            if (contentBuilder.charAt(i) == triggerChar && LOOKUP.indexOf(contentBuilder.charAt(i + 1)) != -1) {
                contentBuilder.delete(i, i + 2);
                breakIndex -= 2;
            }
        }
        return contentBuilder.toString();
    }
}
