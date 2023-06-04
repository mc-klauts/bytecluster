package net.bytemc.cluster.node.console;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fusesource.jansi.Ansi;

@Getter
@AllArgsConstructor
public enum Color {

    BLACK("black", '0', Ansi.ansi().reset().fg(Ansi.Color.BLACK).toString()),
    DARK_BLUE("dark_blue", '1', Ansi.ansi().reset().fg(Ansi.Color.BLUE).toString()),
    GREEN("green", '2', Ansi.ansi().reset().fg(Ansi.Color.GREEN).toString()),
    CYAN("cyan", '3', Ansi.ansi().reset().fg(Ansi.Color.CYAN).toString()),
    DARK_RED("dark_red", '4', Ansi.ansi().reset().fg(Ansi.Color.RED).toString()),
    PURPLE("purple", '5', Ansi.ansi().reset().fg(Ansi.Color.MAGENTA).toString()),
    ORANGE("orange", '6', Ansi.ansi().reset().fg(Ansi.Color.YELLOW).toString()),
    GRAY("gray", '7', Ansi.ansi().reset().fg(Ansi.Color.WHITE).toString()),
    DARK_GRAY("dark_gray", '8', Ansi.ansi().reset().fg(Ansi.Color.BLACK).bold().toString()),
    BLUE("blue", '9', Ansi.ansi().reset().fg(Ansi.Color.BLUE).bold().toString()),
    LIGHT_GREEN("light_green", 'a', Ansi.ansi().reset().fg(Ansi.Color.GREEN).bold().toString()),
    AQUA("aqua", 'b', Ansi.ansi().reset().fg(Ansi.Color.CYAN).bold().toString()),
    RED("red", 'c', Ansi.ansi().reset().fg(Ansi.Color.RED).bold().toString()),
    PINK("pink", 'd', Ansi.ansi().reset().fg(Ansi.Color.MAGENTA).bold().toString()),
    YELLOW("yellow", 'e', Ansi.ansi().reset().fg(Ansi.Color.YELLOW).bold().toString()),
    WHITE("white", 'f', Ansi.ansi().reset().fg(Ansi.Color.WHITE).bold().toString()),
    OBFUSCATED("obfuscated", 'k', Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString()),
    BOLD("bold", 'l', Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString()),
    STRIKETHROUGH("strikethrough", 'm', Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString()),
    UNDERLINE("underline", 'n', Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString()),
    ITALIC("italic", 'o', Ansi.ansi().a(Ansi.Attribute.ITALIC).toString()),
    DEFAULT("default", 'r', Ansi.ansi().reset().toString());

    private final String name;
    private final char id;
    private final String ansi;

    @Override
    public String toString() {
        return ansi;
    }
}
