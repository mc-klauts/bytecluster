package net.bytemc.cluster.node.console;

import lombok.Getter;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.style.StyleResolver;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.logging.Level;

@Getter
public final class ConsoleTerminal {

    private final Terminal terminal;
    private final LineReaderImpl lineReader;
    private String prompt;

    private final boolean supportAnsiSupport;
    private final Lock disallowLog = new ReentrantLock(true);
    private final ConsoleThread consoleThread = new ConsoleThread(this);

    public ConsoleTerminal() {

        this.prompt = ConsoleColorInterpreter.toColoredString('&', "&b cloud &8» &7");

        try {
            java.util.logging.Logger.getLogger("org.jline").setLevel(Level.OFF);
            java.util.logging.Logger.getLogger(StyleResolver.class.getName()).setLevel(Level.OFF);

            this.supportAnsiSupport = ConsoleUtils.installAnsi();

            this.terminal = TerminalBuilder.builder().system(true).encoding(StandardCharsets.UTF_8).build();
            this.lineReader = new LineReaderImpl(this.terminal);

            this.lineReader.option(LineReader.Option.AUTO_GROUP, false);
            this.lineReader.option(LineReader.Option.EMPTY_WORD_OPTIONS, false);
            this.lineReader.option(LineReader.Option.HISTORY_TIMESTAMPED, false);

            this.lineReader.variable(LineReader.BELL_STYLE, "none");
            this.lineReader.variable(LineReader.COMPLETION_STYLE_LIST_BACKGROUND, "inverse");
            this.lineReader.option(LineReader.Option.AUTO_MENU_LIST, true);
            this.lineReader.option(LineReader.Option.AUTO_FRESH_LINE, true);
            this.lineReader.option(LineReader.Option.DISABLE_EVENT_EXPANSION, true);

            this.clearScreen();

            //display header
            ConsoleHeader.print(this);

            this.consoleThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String text) {
        this.forceWrite(text);
    }

    private @NotNull String formatText(String input, String ensureEndsWith) {
        var content = this.supportAnsiSupport ? ConsoleColorInterpreter.toColoredString('&', input) : ConsoleColorInterpreter.stripColor('&', input);
        if (!content.endsWith(ensureEndsWith)) {
            content += ensureEndsWith;
        }
        return content;
    }

    private void redisplay() {
        if (this.lineReader.isReading()) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE);
            this.lineReader.callWidget(LineReader.REDISPLAY);
        }
    }

    public void clearScreen() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.redisplay();
    }

    private void print(String text) {
        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        this.lineReader.getTerminal().puts(InfoCmp.Capability.clr_eol);
        this.lineReader.getTerminal().writer().print(text);
        this.lineReader.getTerminal().writer().flush();
        this.redisplay();
    }

    public void writeEmpty(@NotNull Supplier<String> rawText) {
        this.disallowLog.lock();
        try {
            this.print(this.formatText(rawText.get(), ""));
        } finally {
            this.disallowLog.unlock();
        }
    }


    private void forceWrite(String text) {
        this.disallowLog.lock();
        try {
            var content = this.formatText(text, System.lineSeparator());
            this.print(supportAnsiSupport ? (Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + '\r' + content + Ansi.ansi().reset().toString()) : ('\r' + content));
        } finally {
            this.disallowLog.unlock();
        }
    }

    public void close()  {
        this.consoleThread.interrupt();
        this.terminal.flush();
        try {
            this.terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AnsiConsole.systemUninstall();
    }
}

