package net.bytemc.cluster.api.command.test;

import net.bytemc.cluster.api.command.Command;
import net.bytemc.cluster.api.command.CommandArgument;
import net.bytemc.cluster.api.command.CommandSender;
import net.bytemc.cluster.api.command.SubCommand;
import net.bytemc.cluster.api.command.argument.ArgumentTransformerType;
import org.jetbrains.annotations.NotNull;

@Command(name = "test", aliases = {"t"})
public final class TestCommand {

    @SubCommand(name = "info", example = "test info <name> <full information>")
    public void getGroupInformation(
        @NotNull CommandSender commandSender,
        @CommandArgument(name = "name", transformer = ArgumentTransformerType.STRING, needed = true) String name,
        @CommandArgument(name = "information", transformer = ArgumentTransformerType.BOOLEAN, needed = false) boolean fullInformation
    ) {

        commandSender.sendMessage("");
    }

}
