package net.bytemc.cluster.node.logger;

import net.bytemc.cluster.api.logging.Logger;
import java.io.PrintStream;

public final class NodeOutputPrintStream extends PrintStream {

    private static final PrintStream originalSystemOut = System.out;

    public NodeOutputPrintStream() {
        super(originalSystemOut);
    }

    @Override
    public void println(String line) {
        Logger.info(line);
    }

    @Override
    public void flush() {

    }
}
