package net.bytemc.cluster.node.console;

import lombok.AllArgsConstructor;
import net.bytemc.cluster.api.command.interfaces.CommandSender;
import net.bytemc.cluster.node.Node;
import net.bytemc.cluster.node.console.command.SimpleCommandSender;
import org.fusesource.jansi.Ansi;
import org.jetbrains.annotations.Nullable;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

@AllArgsConstructor
public final class ConsoleThread extends Thread {

    private ConsoleTerminal terminal;
    private final CommandSender commandSender = new SimpleCommandSender();

    @Override
    public void run() {
        this.setName("ByteMC-Console-Reading-Thread");
        this.setPriority(1);

        String line;
        while (!Thread.currentThread().isInterrupted() && (line = this.readLine()) != null) {
            this.terminal.writeEmpty(() -> Ansi.ansi().reset().cursorUp(1).eraseLine().toString());
            Node.getInstance().getCommandExecutor().tryExecution(this.commandSender, line);
        }
    }

    private @Nullable String readLine() {
        try {
            return this.terminal.getLineReader().readLine(this.terminal.getPrompt());
        } catch (EndOfFileException ignored) {
        } catch (UserInterruptException exception) {
            System.exit(-1);
        }
        return null;
    }
}
