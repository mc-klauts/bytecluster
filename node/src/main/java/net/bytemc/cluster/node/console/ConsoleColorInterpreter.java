package net.bytemc.cluster.node.console;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import java.util.regex.Pattern;

public final class ConsoleColorInterpreter {

    private static final Color[] VALUES = Color.values();
    private static final String LOOKUP = "0123456789abcdefklmnor";
    private static final String RGB_ANSI = "\u001B[38;2;%d;%d;%dm";

    public static @NonNull String toColoredString(char triggerChar, @NonNull String input) {
        var contentBuilder = new StringBuilder(convertRGBColors(triggerChar, input));

        var breakIndex = contentBuilder.length() - 1;
        for (var i = 0; i < breakIndex; i++) {
            if (contentBuilder.charAt(i) == triggerChar) {
                var format = LOOKUP.indexOf(contentBuilder.charAt(i + 1));
                if (format != -1) {
                    var ansiCode = VALUES[format].getAnsi();

                    contentBuilder.delete(i, i + 2).insert(i, ansiCode);
                    breakIndex += ansiCode.length() - 2;
                }
            }
        }

        return contentBuilder.toString();
    }

    private static @NonNull String convertRGBColors(char triggerChar, @NonNull String input) {
        var replacePattern = Pattern.compile(triggerChar + "#([\\da-fA-F]){6}");
        return replacePattern.matcher(input).replaceAll(result -> {
            // we could use java.awt.Color but that would load unnecessary native libraries
            int hexInput = Integer.decode(result.group().substring(1));
            return String.format(RGB_ANSI, (hexInput >> 16) & 0xFF, (hexInput >> 8) & 0xFF, hexInput & 0xFF);
        });
    }

    public static @NonNull String stripColor(char triggerChar, @NonNull String input) {
        var contentBuilder = new StringBuilder(stripRGBColors(triggerChar, input));

        var breakIndex = contentBuilder.length() - 1;
        for (var i = 0; i < breakIndex; i++) {
            if (contentBuilder.charAt(i) == triggerChar && LOOKUP.indexOf(contentBuilder.charAt(i + 1)) != -1) {
                contentBuilder.delete(i, i + 2);
                breakIndex -= 2;
            }
        }

        return contentBuilder.toString();
    }

    private static @NonNull String stripRGBColors(char triggerChar, @NonNull String input) {
        var replacePattern = Pattern.compile(triggerChar + "#([\\da-fA-F]){6}");
        return replacePattern.matcher(input).replaceAll("");
    }


    public static @Nullable Color byChar(char index) {
        for (var color : VALUES) {
            if (color.getId() == index) {
                return color;
            }
        }

        return null;
    }

    public static @Nullable Color lastColor(char triggerChar, @NonNull String text) {
        text = text.trim();
        if (text.length() > 2 && text.charAt(text.length() - 2) == triggerChar) {
            return byChar(text.charAt(text.length() - 1));
        }

        return null;
    }
}
