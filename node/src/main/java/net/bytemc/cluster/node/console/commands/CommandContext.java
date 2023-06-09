package net.bytemc.cluster.node.console.commands;

import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Builder
@Getter
public class CommandContext {

    private final CloudCommand command;
    @Singular
    private List<String> arguments;
    @Setter
    @Default
    private boolean passed = true;

}
