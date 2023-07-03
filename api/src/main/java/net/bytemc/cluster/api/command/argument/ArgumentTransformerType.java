package net.bytemc.cluster.api.command.argument;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytemc.cluster.api.command.argument.transformers.BooleanArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.DoubleArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.FloatArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.IntArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.LongArgumentTransformer;
import net.bytemc.cluster.api.command.argument.transformers.PlainTextTransformer;
import net.bytemc.cluster.api.command.argument.transformers.StringArgumentTransformer;

@RequiredArgsConstructor
@Getter
public enum ArgumentTransformerType {

    STRING(new StringArgumentTransformer()),
    INT(new IntArgumentTransformer()),
    LONG(new LongArgumentTransformer()),
    FLOAT(new FloatArgumentTransformer()),
    DOUBLE(new DoubleArgumentTransformer()),
    BOOLEAN(new BooleanArgumentTransformer()),
    PLAIN_TEXT(new PlainTextTransformer());

    private final ArgumentTransformer<?> argumentTransformer;

}
