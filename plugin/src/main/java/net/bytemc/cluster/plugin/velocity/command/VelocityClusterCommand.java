package net.bytemc.cluster.plugin.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.command.CommandRepository;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class VelocityClusterCommand implements SimpleCommand {

    private final CommandRepository commandRepository;

    @Override
    public void execute(@NotNull Invocation invocation) {
        this.commandRepository.findOptional(invocation.alias()).ifPresent(
            indexedCommand -> indexedCommand.execute(
                new VelocityCommandInvoker(invocation.source()),
                new ArrayList<>(List.of(invocation.arguments()))));
    }

    @Override
    public @NotNull CompletableFuture<List<String>> suggestAsync(@NotNull Invocation invocation) {
        return this.commandRepository.findOptional(invocation.alias()).map(indexedCommand -> CompletableFuture.completedFuture(
                indexedCommand.complete(new VelocityCommandInvoker(invocation.source()),
                    new ArrayList<>(List.of(invocation.arguments())))))
            .orElseGet(() -> CompletableFuture.completedFuture(List.of()));
    }


}
