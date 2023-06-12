package net.bytemc.cluster.node.logger;

import net.bytemc.cluster.api.logging.Logger;
import net.bytemc.cluster.node.Node;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class NodeLogger implements Logger {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public void logInfo(String text) {
        print(text);
    }

    public void logWarn(String text) {
        print(text);
    }

    public void logError(String text, Throwable exception) {
        print(text);
        exception.printStackTrace();
    }

    public static void error(String text) {
        Node.getInstance().getConsoleTerminal().write("&8[&7" + DATE_FORMAT.format(new Date()) + "&8] &cerror &8» &7" + text);
    }

    public static void empty() {
        System.out.println(" ");
    }

    public static void debug(String text) {
        print(text);
    }

    private static void print(String text) {
        Node.getInstance().getConsoleTerminal().write("&8[&7" + DATE_FORMAT.format(new Date()) + "&8] » &7" + text);
    }
}
