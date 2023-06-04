package net.bytemc.cluster.node.console;

import org.fusesource.jansi.AnsiConsole;

final class ConsoleUtils {

    public static boolean installAnsi() {
        try {
            AnsiConsole.systemInstall();
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }
}
